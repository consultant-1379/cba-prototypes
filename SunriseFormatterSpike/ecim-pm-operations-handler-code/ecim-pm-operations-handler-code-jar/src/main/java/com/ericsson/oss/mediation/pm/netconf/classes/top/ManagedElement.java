//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.18 at 08:18:54 PM CET 
//
package com.ericsson.oss.mediation.pm.netconf.classes.top;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}managedElementId"/>
 *         &lt;element ref="{}SystemFunctions"/>
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
    "managedElementId",
    "systemFunctions"
})
@XmlRootElement(name = "ManagedElement")
public class ManagedElement {

    @XmlElement(required = true)
    protected String managedElementId;
    @XmlElement(name = "SystemFunctions")
    protected SystemFunctions systemFunctions;

    /**
     * Gets the value of the managedElementId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagedElementId() {
        return managedElementId;
    }

    /**
     * Sets the value of the managedElementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagedElementId(final String value) {
        this.managedElementId = value;
    }

    /**
     * Gets the value of the systemFunctions property.
     * 
     * @return
     *     possible object is
     *     {@link SystemFunctions }
     *     
     */
    public SystemFunctions getSystemFunctions() {
        return systemFunctions;
    }

    /**
     * Sets the value of the systemFunctions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemFunctions }
     *     
     */
    public void setSystemFunctions(final SystemFunctions value) {
        this.systemFunctions = value;
    }

}
