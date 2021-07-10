package com.mygdx.fuegopeligro;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class sha1Hashing {

    public long sha1Hashing(String value) {
        final HashCode hashCode = Hashing.sha1().hashString(value, Charset.defaultCharset());
        return hashCode.asLong();
    }

    public int sha1Int(String value) {
        final HashCode hashCode = Hashing.sha1().hashString(value, Charset.defaultCharset());
        return hashCode.asInt();
    }
}
