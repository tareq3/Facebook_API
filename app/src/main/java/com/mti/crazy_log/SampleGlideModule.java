package com.mti.crazy_log;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class SampleGlideModule  extends AppGlideModule{
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}