package com.kuborros.FurBotNeo.utils.audio.invidious;

import com.sedmelluq.discord.lavaplayer.container.*;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.ProbingAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.io.*;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.info.AudioTrackInfoBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.sedmelluq.discord.lavaplayer.container.MediaContainerDetectionResult.refer;
import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.COMMON;
import static com.sedmelluq.discord.lavaplayer.tools.FriendlyException.Severity.SUSPICIOUS;
import static com.sedmelluq.discord.lavaplayer.tools.io.HttpClientTools.getHeaderValue;

public class InvidiousAudioSourceManager extends ProbingAudioSourceManager implements AudioSourceManager, HttpConfigurable {

    private static final InvidiousAudioGetReference audioGetReference = new InvidiousAudioGetReference("https://www.invidio.us/api/v1/");
    private static final InvidiousAudioSearch audioSearch = new InvidiousAudioSearch("https://www.invidio.us/api/v1/");
    private final HttpInterfaceManager httpInterfaceManager;

    public InvidiousAudioSourceManager() {
        this(MediaContainerRegistry.DEFAULT_REGISTRY);
    }

    private InvidiousAudioSourceManager(MediaContainerRegistry containerRegistry) {
        super(containerRegistry);
        httpInterfaceManager = new ThreadLocalHttpInterfaceManager(
                HttpClientTools
                        .createSharedCookiesHttpBuilder()
                        .setRedirectStrategy(new HttpClientTools.NoRedirectsStrategy()),
                HttpClientTools.DEFAULT_REQUEST_CONFIG
        );
    }

    @Override
    public String getSourceName() {
        return "invidio.us";
    }

    @Override
    public AudioItem loadItem(DefaultAudioPlayerManager manager, AudioReference reference) {
        AudioReference httpReference = getAsHttpReference(reference);
        if (httpReference == null) {
            return null;
        }
        if (httpReference.containerDescriptor != null) {
            return createTrack(AudioTrackInfoBuilder.create(reference, null)
                    .setAuthor(StringUtils.substringAfter(reference.title, "@"))
                    .setTitle(StringUtils.substringBefore(reference.title, "@"))
                    .build(), httpReference.containerDescriptor);
        } else {
            return handleLoadResult(detectContainer(httpReference));
        }
    }

    public AudioTrack createTrack(AudioTrackInfo trackInfo, MediaContainerDescriptor containerDescriptor) {
        return new InvidiousAudioTrack(trackInfo, containerDescriptor, this);
    }

    private AudioReference getAsHttpReference(AudioReference reference) {
        if (reference.identifier.contains("https://youtube") || reference.identifier.startsWith("https://www.youtube") || reference.identifier.startsWith("https://youtu.be")) {
            return getAsIdReference(reference);
        } else if (reference.identifier.startsWith("ytsearch: ")) {
            return getAsSearchReference(reference);
        }
        if ((reference.identifier.contains("https:"))) return reference;
        else return null;
    }

    private AudioReference getAsSearchReference(AudioReference reference) {
        return audioGetReference.getUrlById(audioSearch.getIdBySearch(reference));
    }

    private AudioReference getAsIdReference(AudioReference reference) {
        return audioGetReference.getUrlById(reference);
    }

    private MediaContainerDetectionResult detectContainer(AudioReference reference) {
        MediaContainerDetectionResult result;

        try (HttpInterface httpInterface = getHttpInterface()) {
            result = detectContainerWithClient(httpInterface, reference);
        } catch (IOException e) {
            throw new FriendlyException("Connecting to the URL failed.", SUSPICIOUS, e);
        }

        return result;
    }

    private MediaContainerDetectionResult detectContainerWithClient(HttpInterface httpInterface, AudioReference reference) throws IOException {
        try (PersistentHttpStream inputStream = new PersistentHttpStream(httpInterface, new URI(reference.identifier), Long.MAX_VALUE)) {
            int statusCode = inputStream.checkStatusCode();
            String redirectUrl = HttpClientTools.getRedirectLocation(reference.identifier, inputStream.getCurrentResponse());

            if (redirectUrl != null) {
                return refer(null, new AudioReference(redirectUrl, StringUtils.substringBefore(reference.title, "@")));
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                return null;
            } else if (!HttpClientTools.isSuccessWithContent(statusCode)) {
                throw new FriendlyException("That URL is not playable.", COMMON, new IllegalStateException("Status code " + statusCode));
            }

            MediaContainerHints hints = MediaContainerHints.from(getHeaderValue(inputStream.getCurrentResponse(), "Content-Type"), null);
            return new MediaContainerDetection(containerRegistry, reference, inputStream, hints).detectContainer();
        } catch (URISyntaxException e) {
            throw new FriendlyException("Not a valid URL.", COMMON, e);
        }
    }

    @Override
    protected AudioItem handleLoadResult(MediaContainerDetectionResult result) {
        if (result != null) {
            if (result.isReference()) {
                return result.getReference();
            } else if (!result.isContainerDetected()) {
                throw new FriendlyException("Unknown file format. Youtube might have changed formats.", COMMON, null);
            } else if (!result.isSupportedFile()) {
                throw new FriendlyException(result.getUnsupportedReason(), COMMON, null);
            } else {
                return createTrack(result.getTrackInfo(), result.getContainerDescriptor());
            }
        }

        return null;
    }

    @Override
    public boolean isTrackEncodable(AudioTrack track) {
        return true;
    }

    @Override
    public void encodeTrack(AudioTrack track, DataOutput output) throws IOException {
        encodeTrackFactory(((InvidiousAudioTrack) track).getContainerTrackFactory(), output);
    }

    @Override
    public AudioTrack decodeTrack(AudioTrackInfo trackInfo, DataInput input) throws IOException {
        MediaContainerDescriptor containerTrackFactory = decodeTrackFactory(input);

        if (containerTrackFactory != null) {
            return new InvidiousAudioTrack(trackInfo, containerTrackFactory, this);
        }

        return null;
    }

    @Override
    public void shutdown() {

    }

    HttpInterface getHttpInterface() {
        return httpInterfaceManager.getInterface();
    }

    @Override
    public void configureRequests(Function<RequestConfig, RequestConfig> configurator) {
        httpInterfaceManager.configureRequests(configurator);
    }

    @Override
    public void configureBuilder(Consumer<HttpClientBuilder> configurator) {
        httpInterfaceManager.configureBuilder(configurator);
    }

}
