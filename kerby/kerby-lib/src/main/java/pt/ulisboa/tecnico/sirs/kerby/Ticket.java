package pt.ulisboa.tecnico.sirs.kerby;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;


import javax.crypto.KeyGenerator;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;

/**
 * Class that represents a Kerberos ticket and can use different data formats.
 * 
 * @author Miguel Pardal
 *
 */
public class Ticket {

	/** Ticket data container. After creation, cannot be null. */
	private TicketView view;

    private Key dataPrivacyKey = null;

	// ticket creation -------------------------------------------------------

	/** Create ticket from arguments. */
	public Ticket(String x, String y, Date time1, Date time2, Key key) {
        try {
            dataPrivacyKey = SecurityHelper.generateKeyFromPassword("A564CC6E84FB5B77DAA4A2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        view = new TicketView();
		view.setX(x);
		view.setY(y);
		view.setTime1(XMLHelper.dateToXML(time1));
		view.setTime2(XMLHelper.dateToXML(time2));
		view.setEncodedKeyXY(key.getEncoded());
		view.setDataPrivacyKey(dataPrivacyKey.getEncoded());
	}

	// TODO create constructor without key (one is generated)

	// TODO create constructor without times (defaults e.g. now and now+60sec are
	// created)

	// create constructor without key and times
	protected Ticket() {
		//For tests
	}

	/** Create ticket from data view. */
	public Ticket(TicketView view) {
		setView(view);
	}

	/** Create ticket from ciphered data view and key. */
	public Ticket(CipheredView cipheredView, Key key) throws KerbyException {
		decipher(cipheredView, key);
	}

	// After construction, view can never be null, and can never be set to null.
	// This invariant is assumed to be true in the remaining code.

	// accessors -------------------------------------------------------------

	protected TicketView getView() {
		return view;
	}

	protected void setView(TicketView view) {
		if (view == null)
			throw new IllegalArgumentException("View cannot be null!");
		this.view = view;
	}

	public String getX() {
		return view.getX();
	}

	public void setX(String x) {
		view.setX(x);
	}

	public String getY() {
		return view.getY();
	}

	public void setY(String y) {
		view.setY(y);
	}

	public Date getTime1() {
		Date time = XMLHelper.xmlToDate(view.getTime1());
		return time;
	}

	public void setTime1(Date time1) {
		XMLGregorianCalendar xgc = XMLHelper.dateToXML(time1);
		view.setTime1(xgc);
	}

	public Date getTime2() {
		Date time = XMLHelper.xmlToDate(view.getTime2());
		return time;
	}

	public void setTime2(Date time2) {
		XMLGregorianCalendar xgc = XMLHelper.dateToXML(time2);
		view.setTime2(xgc);
	}

	public Key getKeyXY() {
		byte[] encodedKeyXY = view.getEncodedKeyXY();
		Key key = SecurityHelper.recodeKey(encodedKeyXY);
		return key;
	}

	public void setKeyXY(Key keyXY) {
		byte[] encodedKey = keyXY.getEncoded();
		view.setEncodedKeyXY(encodedKey);
	}

    public Key getDataPrivacyKey() {
        byte[] encodedPrivacyKey = view.getDataPrivacyKey();
        Key key = SecurityHelper.recodeKey(encodedPrivacyKey);
        return key;
    }

    public void setDataPrivacyKey(Key dataPrivacyKey) {
		byte[] encodedKey = dataPrivacyKey.getEncoded();
		view.setDataPrivacyKey(encodedKey);
    }

	// object methods --------------------------------------------------------

	/** Create a textual representation of the TicketView. */
	public String toString() {
		if (view == null)
			return "null";

		StringBuilder builder = new StringBuilder();
		builder.append("TicketView [x=");
		builder.append(view.getX());
		builder.append(", y=");
		builder.append(view.getY());
		builder.append(", time1=");
		builder.append(view.getTime1());
		builder.append(", time2=");
		builder.append(view.getTime2());
		builder.append(", encodedKeyXY=");
		builder.append(Arrays.toString(view.getEncodedKeyXY()));
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(view.getEncodedKeyXY());
		result = prime * result + ((view.getTime1() == null) ? 0 : view.getTime1().hashCode());
		result = prime * result + ((view.getTime2() == null) ? 0 : view.getTime2().hashCode());
		result = prime * result + ((view.getX() == null) ? 0 : view.getX().hashCode());
		result = prime * result + ((view.getY() == null) ? 0 : view.getY().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		TicketView otherView = other.getView();

		return ticketViewEquals(view, otherView);
	}

	/** Compare contents of two ticket views. */
	protected boolean ticketViewEquals(TicketView view1, TicketView view2) {
		if (view1 == view2)
			return true;
		if (view2 == null)
			return false;
		if (view1.getClass() != view2.getClass())
			return false;
		if (view1.getEncodedKeyXY() == null) {
			if (view2.getEncodedKeyXY() != null)
				return false;
		} else if (view2.getEncodedKeyXY() == null || !Arrays.equals(view1.getEncodedKeyXY(), view2.getEncodedKeyXY()))
			// compare keys in encoded form
			// uses Arrays.equals to compare array contents
			return false;
		if (view1.getTime1() == null) {
			if (view2.getTime1() != null)
				return false;
		} else if (!view1.getTime1().equals(view2.getTime1()))
			return false;
		if (view1.getTime2() == null) {
			if (view2.getTime2() != null)
				return false;
		} else if (!view1.getTime2().equals(view2.getTime2()))
			return false;
		if (view1.getX() == null) {
			if (view2.getX() != null)
				return false;
		} else if (!view1.getX().equals(view2.getX()))
			return false;
		if (view1.getY() == null) {
			if (view2.getY() != null)
				return false;
		} else if (!view1.getY().equals(view2.getY()))
			return false;
		return true;
	}

	// ticket validation -----------------------------------------------------

	/** Validate contents of TicketView. */
	public void validate() throws KerbyException {

		// check nulls and empty strings
		if (view == null)
			throw new KerbyException("Null ticket!");

		final String x = view.getX();
		if (x == null || x.trim().length() == 0)
			throw new KerbyException("X cannot be empty!");

		final String y = view.getY();
		if (y == null || y.trim().length() == 0)
			throw new KerbyException("Y cannot be empty!");

		final XMLGregorianCalendar xgc1 = view.getTime1();
		if (xgc1 == null)
			throw new KerbyException("Time 1 cannot be empty!");

		final XMLGregorianCalendar xgc2 = view.getTime2();
		if (xgc2 == null)
			throw new KerbyException("Time 2 cannot be empty!");

		final byte[] encodedKeyXY = view.getEncodedKeyXY();
		if (encodedKeyXY == null)
			throw new KerbyException("Encoded key cannot be empty!");

		// check times
		int result = xgc1.toGregorianCalendar().compareTo(xgc2.toGregorianCalendar());
		// if result is positive, then date1 is "later" than date2
		if (result > 0)
			throw new KerbyException("Time 2 of ticket must be after time 1!");

		// does not check if key encoding is correct to avoid having one conversion here
		// at
		// validation, and one later at the getter when key is needed for use
	}

	// XML serialization -----------------------------------------------------

	/**
	 * Marshal ticket to XML document.
	 */
	public Node toXMLNode(String ticketTagName) throws JAXBException {
		return XMLHelper.viewToXML(TicketView.class, view, new QName(ticketTagName));
	}

	/** Marshal ticket to XML bytes. */
	public byte[] toXMLBytes(String ticketTagName) throws JAXBException {
		return XMLHelper.viewToXMLBytes(TicketView.class, view, new QName(ticketTagName));
	}

	/**
	 * Unmarshal ticket from XML document.
	 */
	public void fromXMLNode(Node xml) throws JAXBException {
		TicketView view = XMLHelper.xmlNodeToView(TicketView.class, xml);
		// set view should not allow null
		setView(view);
	}

	/** Unmarshal byte array to a view object. */
	public void fromXMLBytes(byte[] bytes) throws JAXBException {
		TicketView view = XMLHelper.xmlBytesToView(TicketView.class, bytes);
		// set view should not allow null
		setView(view);
	}

	// ciphering ---------------------------------------------------------------

	public CipheredView cipher(Key key) throws KerbyException {
		return SecurityHelper.cipher(TicketView.class, view, key);
	}

	/**
	 * Decipher is private because it should only be called by constructor when
	 * receiving a CipheredView of the ticket.
	 */
	private void decipher(CipheredView cipheredView, Key key) throws KerbyException {
		TicketView view = SecurityHelper.decipher(TicketView.class, cipheredView, key);
		// set view should not allow null
		setView(view);
	}

}
