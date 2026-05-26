package pchy.pchy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String subirFoto(MultipartFile archivo, String nombreArchivo)
            throws IOException {

        Map resultado = cloudinary.uploader().upload(
            archivo.getBytes(),
            ObjectUtils.asMap(
                "public_id",   "pchy/perfiles/" + nombreArchivo,
                "overwrite",   true,
                "folder",      "pchy/perfiles"
            )
        );

        return (String) resultado.get("secure_url");
    }

    public void eliminarFoto(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}