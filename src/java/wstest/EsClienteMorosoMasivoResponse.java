
package wstest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EsClienteMorosoMasivoResult" type="{https://webservices.electricaribe.com/ws_sicomercial/}ArrayOfString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "esClienteMorosoMasivoResult"
})
@XmlRootElement(name = "EsClienteMorosoMasivoResponse")
public class EsClienteMorosoMasivoResponse {

    @XmlElement(name = "EsClienteMorosoMasivoResult")
    protected ArrayOfString esClienteMorosoMasivoResult;

    /**
     * Obtiene el valor de la propiedad esClienteMorosoMasivoResult.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getEsClienteMorosoMasivoResult() {
        return esClienteMorosoMasivoResult;
    }

    /**
     * Define el valor de la propiedad esClienteMorosoMasivoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setEsClienteMorosoMasivoResult(ArrayOfString value) {
        this.esClienteMorosoMasivoResult = value;
    }

}
