/*
 * Created On:  Sunday December 17, 2006 14:53
 */
package com.thinkparity.codebase.model.user;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.XPP3Reader;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <b>Title:</b>thinkParity User VCard<br>
 * <b>Description:</b>An abstraction of a user object's interface into their
 * virtual card.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class UserVCard {

    /** A dom4j <code>DocumentFactory</code>. */
    private static final DocumentFactory DOCUMENT_FACTORY;
   
    /** A dom4j <code>XPP3Reader</code>. */
    private static final XPP3Reader XPP_READER;

    static {
        DOCUMENT_FACTORY = DocumentFactory.getInstance();
        XPP_READER = new XPP3Reader();
    }

    /** The dom4j <code>Document</code>. */
    private Document document;

    /**
     * Create UserVCard.
     *
     */
    protected UserVCard() {
        super();
    }

    /**
     * Obtain the city.
     * 
     * @return The city <code>String</code>.
     */
    public String getCity() {
        return getNodeText(VCardField.CITY);
    }

    /**
     * Obtain the country.
     * 
     * @return The country <code>String</code>.
     */
    public String getCountry() {
        return getNodeText(VCardField.COUNTRY);
    }

    /**
     * Obtain the mobile phone.
     * 
     * @return The mobile phone <code>String</code>.
     */
    public String getMobilePhone() {
        return getNodeText(VCardField.MOBILE_PHONE);
    }

    /**
     * Obtain the organization.
     * 
     * @return The organization <code>String</code>.
     */
    public String getOrganization() {
        return getNodeText(VCardField.ORGANIZATION);
    }

    /**
     * Obtain the phone number.
     * 
     * @return The phone number <code>String</code>.
     */
    public String getPhone() {
        return getNodeText(VCardField.PHONE);
    }

    /**
     * Obtain the province.
     * 
     * @return The province <code>String</code>.
     */
    public String getProvince() {
        return getNodeText(VCardField.PROVINCE);
    }

    /**
     * Obtain the timezone.
     * 
     * @return The timezone <code>String</code>.
     */
    public String getTimezone() {
        return getNodeText(VCardField.TIMEZONE);
    }

    /**
     * Obtain the title.
     * 
     * @return The title <code>String</code>.
     */
    public String getTitle() {
        return getNodeText(VCardField.TITLE);
    }


    /**
     * Obtain the vcard xml.
     * 
     * @return The vcard xml <code>String</code>.
     */
    public String getVCardXML() {
        return document.asXML();
    }

    /**
     * Determine if the city is set.
     * 
     * @return True if the city is set.
     */
    public Boolean isSetCity() {
        return isSetNodeText(VCardField.CITY);
    }

    /**
     * Determine if the country is set.
     * 
     * @return True if the country is set.
     */
    public Boolean isSetCountry() {
        return isSetNodeText(VCardField.COUNTRY);
    }

    /**
     * Determine if the mobile phone is set.
     * 
     * @return True if the mobile phone is set.
     */
    public Boolean isSetMobilePhone() {
        return isSetNodeText(VCardField.MOBILE_PHONE);
    }

    /**
     * Determine if the organization is set.
     * 
     * @return True if the organization is set.
     */    
    public Boolean isSetOrganization() {
        return isSetNodeText(VCardField.ORGANIZATION);
    }

    /**
     * Determine if the mobile phone is set.
     * 
     * @return True if the mobile phone is set.
     */
    public Boolean isSetPhone() {
        return isSetNodeText(VCardField.PHONE);
    }

    /**
     * Determine if the province is set.
     * 
     * @return True if the province is set.
     */
    public Boolean isSetProvince() {
        return isSetNodeText(VCardField.PROVINCE);
    }

    /**
     * Obtain the timezone.
     * 
     * @return The timezone <code>String</code>.
     */
    public Boolean isSetTimezone() {
        return isSetNodeText(VCardField.TIMEZONE);
    }

    /**
     * Determine if the title is set.
     * 
     * @return True if the title is set.
     */    
    public Boolean isSetTitle() {
        return isSetNodeText(VCardField.TITLE);
    }

    /**
     * Set the city.
     * 
     * @param city
     *            The city <code>String</code>.
     */
    public void setCity(final String city) {
        setNodeText(VCardField.CITY, city);
    }

    /**
     * Set the country.
     * 
     * @param country
     *            The country <code>String</code>.
     */
    public void setCountry(final String country) {
        setNodeText(VCardField.COUNTRY, country);
    }

    /**
     * Set the mobile phone.
     * 
     * @param mobilePhone
     *            The mobile phone number <code>String</code>.
     */
    public void setMobilePhone(final String mobilePhone) {
        setNodeText(VCardField.MOBILE_PHONE, mobilePhone);
    }

    /**
     * Set the vcard name.
     * 
     * @param given
     *            The given name <code>String</code>.
     * @param middle
     *            The middle name <code>String</code>.
     * @param family
     *            The family name <code>String</code>.
     */
    public void setName(final String name) {
        final UserNameTokenizer tokenizer = new UserNameTokenizer(name);
        setNodeText(VCardField.GIVEN, tokenizer.getGiven());
        setNodeText(VCardField.MIDDLE, tokenizer.getMiddle());
        setNodeText(VCardField.FAMILY, tokenizer.getFamily());
        setNodeText(VCardField.FULL_NAME, name);
    }
    /**
     * Set the organization.
     * 
     * @param organization
     *            The organization <code>String</code>.
     */
    public void setOrganization(final String organization) {
        setNodeText(VCardField.ORGANIZATION, organization);
    }

    /**
     * Set the phone.
     * 
     * @param phone
     *            The phone number <code>String</code>.
     */
    public void setPhone(final String phone) {
        setNodeText(VCardField.PHONE, phone);
    }

    /**
     * Set the province.
     * 
     * @param province
     *            The province <code>String</code>.
     */
    public void setProvince(final String province) {
        setNodeText(VCardField.PROVINCE, province);
    }

    /**
     * Obtain the timezone.
     * 
     * @return The timezone <code>String</code>.
     */
    public void setTimezone(final String timezone) {
        setNodeText(VCardField.TIMEZONE, timezone);
    }

    /**
     * Set the title.
     * 
     * @param title
     *            The title <code>String</code>.
     */
    public void setTitle(final String title) {
        setNodeText(VCardField.TITLE, title);
    }

    /**
     * Set the vcard xml.
     * 
     * @param xml
     *            The vcard xml <code>String</code>.
     */
    public void setVCardXML(final String xml) {
        final StringReader xmlReader = new StringReader(xml);
        try {
            this.document = XPP_READER.read(xmlReader);
        } catch (final DocumentException dx) {
            throw new IllegalArgumentException(dx);
        } catch (final IOException iox) {
            throw new IllegalArgumentException(iox);
        } catch (final XmlPullParserException xppx) {
            throw new IllegalArgumentException(xppx);
        } finally {
            xmlReader.close();
        }
    }

    /**
     * Create a node for a vcard field and add it to the document.
     * 
     * @param vcardField
     *            A <code>VCardField</code>.
     * @return A dom4j <code>Node</code>.
     */
    private Node createNode(final VCardField vcardField) {
        Element element = null, parent = document.getRootElement();
        for (final String pathName : vcardField.getXPathNames()) {
            element = parent.element(pathName);
            if (null == element) {
                element = DOCUMENT_FACTORY.createElement(new QName(pathName));
                parent.add(element);
            }
            parent = element;
        }
        return element;
    }

    /**
     * Obtain the node for the vcard field.
     * 
     * @param vcardField
     *            A <code>VCardField</code>.
     * @return A dom4j <code>Node</code>.
     */
    private Node getNode(final VCardField vcardField) {
        final Element root = document.getRootElement();
        return root.selectSingleNode(vcardField.xpath);
    }

    /**
     * Obtain the vcard field's node text.
     * 
     * @param vcardField
     *            A <code>VCardField</code>.
     * @return The vcard field's node text <code>String</code>.
     */
    private String getNodeText(final VCardField vcardField) {
        final Node node = getNode(vcardField);
        if (null == node) {
            return null;
        } else {
            return node.getText();
        }
    }

    /**
     * Determine if a vcard field's node is set.
     * 
     * @param vcardField
     *            A <code>VCardField</code>.
     * @return True if it is set.
     */
    private Boolean isSetNode(final VCardField vcardField) {
        return Boolean.valueOf(null != getNode(vcardField));
    }

    /**
     * Determine if a node text is set.
     * 
     * @param xpathExpression
     *            An <code>XPathExpression</code>.
     * @return True if the node text is set.
     */
    private Boolean isSetNodeText(final VCardField vcardField) {
        return Boolean.valueOf(null != getNodeText(vcardField));
    }

    /**
     * Set a node value.
     * 
     * @param vcardField
     *            A <code>VCardField</code>.
     * @param value
     *            A field value.
     */
    private void setNodeText(final VCardField vcardField, final String value) {
        if (null == value) {
            if (isSetNode(vcardField)) {
                final Node node = getNode(vcardField);
                final Element parent = node.getParent();
                parent.remove(node);
            }
        } else {
            if (isSetNode(vcardField)) {
                getNode(vcardField).setText(value);
            } else {
                createNode(vcardField).setText(value);
            }
        }
    }

    /**
     * <b>Title:</b>thinkParity VCard Field<br>
     * <b>Description:</b>Represents the vcard field and hierarchy within the
     * xml document.<br>
     */
    private enum VCardField {

        CITY("ADR/WORK/LOCALITY"), COUNTRY("ADR/WORK/CTRY"), FAMILY("N/FAMILY"),
        FULL_NAME("FN"),  GIVEN("N/GIVEN"), MIDDLE("N/MIDDLE"),
        MOBILE_PHONE("TEL/CELL/WORK/NUMBER"), ORGANIZATION("ORG/ORGNAME"),
        PHONE("TEL/VOICE/WORK/NUMBER"), PROVINCE("ADR/WORK/REGION"),
        TIMEZONE("TZ"), TITLE("TITLE");

        /** The vcard field xpath <code>String</code>. */
        private final String xpath;

        /**
         * Create VCardField.
         * 
         * @param xpath
         *            The xpath <code>String</code>.
         */
        private VCardField(final String xpath) {
            this.xpath = xpath;
        }

        /**
         * Obtain a list of xpath names from the vcard field to the root.
         * 
         * @return A <code>List</code> of individual xpath element names.
         */
        private List<String> getXPathNames() {
            final StringTokenizer xpathTokenizer = new StringTokenizer(xpath, "/");
            final List<String> xpathNames = new ArrayList<String>(xpathTokenizer.countTokens());
            while (xpathTokenizer.hasMoreTokens()) {
                xpathNames.add(xpathTokenizer.nextToken());
            }
            return xpathNames;
        }
    }
}
