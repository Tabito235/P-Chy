// Crea: src/main/java/pchy/pchy/util/InstitucionUtil.java
package pchy.pchy.util;

public class InstitucionUtil {

    public static String obtenerImagen(String institucion) {
        if (institucion == null) return "/Imagenes/Pichy.png";

        switch (institucion) {
            case "ESCOM":           return "/Imagenes/instituciones/escom.png";
            case "UPIITA":          return "/Imagenes/instituciones/upiita.png";
            case "ESIME Zacatenco":
            case "ESIME Culhuacán": return "/Imagenes/instituciones/esime.jpg";
            case "UPIIZ":           return "/Imagenes/instituciones/upiiz.png";
            case "UPIICSA":         return "/Imagenes/instituciones/upiicsa.png";
            case "IPN":             return "/Imagenes/instituciones/upiiz.png";
            case "UNAM":
            case "Facultad de Ingeniería":
            case "Facultad de Ciencias":
            case "FES Aragón":
            case "FES Acatlán":
            case "FES Cuautitlán":  return "/Imagenes/instituciones/unam.png";
            case "TecNM":
            case "IT Zacatecas":
            case "IT León":
            case "IT Tijuana":
            case "IT Durango":      return "/Imagenes/instituciones/Tecnm.jpg";
            case "UAZ":             return "/Imagenes/instituciones/uaz.png";
            case "UANL":            return "/Imagenes/instituciones/uanl.png";
            case "UDG":             return "/Imagenes/instituciones/udg.png";
            case "BUAP":            return "/Imagenes/instituciones/buap.png";
            case "UAM":
            case "Azcapotzalco":
            case "Iztapalapa":
            case "Cuajimalpa":
            case "Lerma":
            case "Xochimilco":      return "/Imagenes/instituciones/UAM.png";
            case "Tec Monterrey":   return "/Imagenes/instituciones/Mapach.png";
            default:                return "/Imagenes/Pichy.png";
        }
    }
}