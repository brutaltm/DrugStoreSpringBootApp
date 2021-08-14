package edu.uph.ii.springbootprj.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Base64Utils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Getter @Setter
public class PhotoDesc {
    private String fileName;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] content;

    public String getContentBase64() {
        return Base64Utils.encodeToString(content);
    }
}
