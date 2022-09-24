package de.samply.teiler.singlespa;

import de.samply.teiler.core.TeilerCoreConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class SingleSpaLinkGenerator {

    private String projectOrganisation;

    public SingleSpaLinkGenerator(@Value(TeilerCoreConst.PROJECT_ORGANISATION_SV) String projectOrganisation) {
        this.projectOrganisation = projectOrganisation;
    }

    public String generateSingleSpaLink(String teilerAppName) {
        return '@' + projectOrganisation + '/' + teilerAppName;
    }

    public String generateSingleSpaLink(String teilerAppName, String language) {
        return '@' + projectOrganisation + '/' + language.toLowerCase() + '/' + teilerAppName;
    }

    public String generateSingleSpaSourceLinkForRootConfig(String rootConfigSourceUrl) {
        return createUrl(rootConfigSourceUrl, projectOrganisation + '-' + TeilerCoreConst.SINGLE_SPA_ROOT_CONFIG + ".js");
    }

    public String generateSingleSpaSourceLink(String teilerAppSourceUrl) {
        return generateSingleSpaSourceLink(teilerAppSourceUrl, null);
    }

    public String generateSingleSpaSourceLink(String teilerAppSourceUrl, String teilerAppSingleSpaMainJs) {
        return createUrl(teilerAppSourceUrl, (teilerAppSingleSpaMainJs != null) ? teilerAppSingleSpaMainJs : TeilerCoreConst.SINGLE_SPA_DEFAULT_MAIN_JS);
    }

    private String createUrl(String baseUrl, String path) {
        try {
            return (baseUrl != null) ? UriComponentsBuilder
                    .fromHttpUrl(baseUrl)
                    .path('/' + path)
                    .toUriString()
                    .substring(new URL(baseUrl).getProtocol().length() + 1) : null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
