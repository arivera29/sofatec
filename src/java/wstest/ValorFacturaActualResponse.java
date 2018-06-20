
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
 *         &lt;element name="ValorFacturaActualResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "valorFacturaActualResult"
})
@XmlRootElement(name = "ValorFacturaActualResponse")
public class ValorFacturaActualResponse {

    @XmlElement(name = "ValorFacturaActualResult")
    protected String valorFacturaActualResult;

    /**
     * Obtiene el valor de la propiedad valorFacturaActualResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorFacturaActualResult() {
        return valorFacturaActualResult;
    }

    /**
     * Define el valor de la propiedad valorFacturaActualResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorFacturaActualResult(String value) {
        this.valorFacturaActualResult = value;
    }

}
