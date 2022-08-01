package de.samply.teiler.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TeilerCoreServices {

    private String nNgmUrl;
    private String qualityReportUrl;

    public String getnNgmUrl() {
        return nNgmUrl;
    }

    @Autowired
    public void setnNgmUrl(@Value(TeilerCoreConst.NNGM_URL_SV) String nNgmUrl) {
        this.nNgmUrl = nNgmUrl;
    }

    public String getQualityReportUrl() {
        return qualityReportUrl;
    }

    @Autowired
    public void setQualityReportUrl(@Value(TeilerCoreConst.QUALITY_REPORT_URL_SV) String qualityReportUrl) {
        this.qualityReportUrl = qualityReportUrl;
    }

}
