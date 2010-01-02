// See the COPYRIGHT file for copyright and license information
package org.xins.logdoc;

import java.io.IOException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;


import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Entity/URI resolver that can be used during XML parsing and during XSLT
 * transformation.
 *
 * @author <a href="mailto:ernst@ernstdehaan.com">Ernst de Haan</a>
 */
class Resolver implements EntityResolver, URIResolver {
   
   // TODO: Cache the DTD information internally
   // TODO: Consider supporting older DTDs as well

   //-------------------------------------------------------------------------
   // Class fields
   //-------------------------------------------------------------------------

   /**
    * The URL to the local <code>log_3_0</code> DTD. Initialized by the class
    * initializer.
    */
   private static final URL LOG_DTD_URL;
   

   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   /**
    * Class initializer that reads the Logdoc 3.0 DTD into memory.
    */
   static {
      // TODO: Move this to the Library class
      LOG_DTD_URL = Library.getMetaResource("dtd/log_3_0.dtd");
      if (LOG_DTD_URL == null) {
         throw new Error("Failed to load log_3_0.dtd file.");
      }
   }

   
   //-------------------------------------------------------------------------
   // Constructors
   //-------------------------------------------------------------------------

   /**
    * Constructs a new <code>LogdocResolver</code>.
    */
   Resolver() {
      // empty
   }

   
   //-------------------------------------------------------------------------
   // Class functions
   //-------------------------------------------------------------------------

   public InputSource resolveEntity(String publicId, String systemId)
	throws SAXException, IOException {
      System.err.println("LogdocResolver.resolveEntity called with publicId=\"" + publicId + "\" and systemId=\"" + systemId + "\".");
      if ("-//Logdoc//DTD Logdoc 3.0//EN".equals(publicId)) {
         return new InputSource(LOG_DTD_URL.openStream());
      }
		return null;
	}

	public Source resolve(String href, String base) throws TransformerException {
	   String resultURL = "/META-INF/xslt/" + href;
      System.err.println("LogdocResolver.resolve called with href=\"" + href + "\" and base=\"" + base + "\"; result is \"" + resultURL + "\".");
		return new StreamSource(Resolver.class.getResourceAsStream(resultURL));
	}
}
