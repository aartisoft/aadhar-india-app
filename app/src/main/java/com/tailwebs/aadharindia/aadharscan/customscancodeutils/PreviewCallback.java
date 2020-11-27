package com.tailwebs.aadharindia.aadharscan.customscancodeutils;

/**
 * Callback for camera previews.
 */
public interface PreviewCallback {
    void onPreview(SourceData sourceData);
    void onPreviewError(Exception e);
}
