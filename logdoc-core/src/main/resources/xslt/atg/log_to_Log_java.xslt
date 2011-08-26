<?xml version="1.0" encoding="UTF-8" ?>
<!-- See the COPYRIGHT file for redistribution and use restrictions. -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Define parameters -->
	<xsl:param name="package_name" />
	<xsl:param name="accesslevel"  />

	<!-- Define variables -->

	<!-- Perform includes -->
	<xsl:include href="shared.xslt" />

	<!-- Set output method -->
	<xsl:output method="text" />

	<xsl:template match="log">

		<xsl:variable name="accessmodifier">
			<xsl:choose>
				<xsl:when test="(string-length($accesslevel) = 0) or $accesslevel = 'package'" />
				<xsl:when test="$accesslevel = 'public'">public </xsl:when>
			</xsl:choose>
		</xsl:variable>

		<xsl:text>package </xsl:text>
		<xsl:value-of select="$package_name" />
		<xsl:text>;

/**
 * Central logging handler.</xsl:text>
      <xsl:if test="string-length(@since) &gt; 0">
         <xsl:text>
 *
 * @since </xsl:text>
         <xsl:value-of select="@since" />
      </xsl:if>
      <xsl:text>
 */
</xsl:text>
		<xsl:value-of select="$accessmodifier" />
		<xsl:text>class Log extends org.znerd.logdoc.internal.atg.AbstractLog {

   /**
    * The fully-qualified name for this class.
    */
   private static final String FQCN = "</xsl:text>
		<xsl:value-of select="$package_name" />
		<xsl:text><![CDATA[.Log";

   /**
    * Controller for this <em>logdoc</em> <code>Log</code> class.
    */
   private static final Controller CONTROLLER;

   /**
    * Associations from name to translation bundle.
    */
   private static final java.util.HashMap<String,TranslationBundle> TRANSLATION_BUNDLES_BY_NAME;

   /**
    * The active translation bundle.
    */
   private static TranslationBundle TRANSLATION_BUNDLE;]]></xsl:text>

		<xsl:text><![CDATA[

   /**
    * Initializes this class.
    */
   static {

      // Reference all translation bundles by name
      TRANSLATION_BUNDLES_BY_NAME = new java.util.HashMap<String,TranslationBundle>();]]></xsl:text>
			<xsl:for-each select="translation-bundle">
				<xsl:text>
      TRANSLATION_BUNDLES_BY_NAME.put("</xsl:text>
				<xsl:value-of select="@locale" />
				<xsl:text>", TranslationBundle_</xsl:text>
				<xsl:value-of select="@locale" />
				<xsl:text>.SINGLETON);</xsl:text>
			</xsl:for-each>
			<xsl:text><![CDATA[

      // Create LogController instance
      CONTROLLER = new Controller();
   }

   /**
    * Constructor for this class. Intentionally made <code>private</code>,
    * since no instances of this class should be created. Instead, the class
    * functions should be used.
    */
   private Log() {
      // empty
   }

   /**
    * Retrieves the active translation bundle.
    *
    * @return
    *    the translation bundle that is currently in use, never
    *    <code>null</code>.
    */
   public static final TranslationBundle getTranslationBundle() {
      return TRANSLATION_BUNDLE;
   }]]></xsl:text>

		<xsl:apply-templates select="group/entry" />

		<xsl:text><![CDATA[

   /**
    * Controller for this <code>Log</code> class.
    */
   private static final class Controller extends org.znerd.logdoc.internal.LogController {

      /**
       * Constructs a new <code>Controller</code> for this log.
       *
       * @throws org.znerd.logdoc.UnsupportedLocaleException
       *    if the current locale is unsupported.
       */
      public Controller() throws org.znerd.logdoc.UnsupportedLocaleException {
         super();
      }

      @Override
      public String toString() {
         return getClass().getName();
      }

      @Override
      public boolean isLocaleSupported(String locale) {

         // Return true if the bundle exists
         return TRANSLATION_BUNDLES_BY_NAME.containsKey(locale);
      }

      @Override
      public void setLocale(String newLocale) {
         TRANSLATION_BUNDLE = TRANSLATION_BUNDLES_BY_NAME.get(newLocale);
      }
   }
}
]]></xsl:text>
	</xsl:template>

	<xsl:template match="group/entry">
		<xsl:variable name="category"       select="concat($package_name, '.', ../@id, '.', @id)" />
		<xsl:variable name="exception"      select="@exception = 'true'" />
		<xsl:variable name="exceptionClass">
			<xsl:choose>
				<xsl:when test="string-length(@exceptionClass) &gt; 0">
					<xsl:value-of select="@exceptionClass" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>java.lang.Throwable</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:text>

   /**
    * Logs the entry with ID </xsl:text>
		<xsl:value-of select="@id" />
		<xsl:text><![CDATA[, in the log entry group <em>]]></xsl:text>
		<xsl:value-of select="../@name" />
		<xsl:text><![CDATA[</em>.
    * The description for this log entry is:
    * <blockquote><em>]]></xsl:text>
		<xsl:apply-templates select="description" />
		<xsl:text><![CDATA[</em></blockquote>
    */
   public static final void log_]]></xsl:text>
		<xsl:value-of select="@id" />
		<xsl:text>(atg.nucleus.logging.ApplicationLogging logger</xsl:text>
		<xsl:if test="$exception">
			<xsl:text>, </xsl:text>
			<xsl:value-of select="$exceptionClass" />
			<xsl:text> _exception</xsl:text>
		</xsl:if>
		<xsl:apply-templates select="param" mode="method-argument">
      <xsl:with-param name="comma" select="true()" />
    </xsl:apply-templates>
		<xsl:text>) {</xsl:text>
		<xsl:for-each select="param[@filter='true']">
			<xsl:text>
      </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text> = org.znerd.logdoc.Library.getLogFilter().filter("</xsl:text>
			<xsl:value-of select="$category" />
			<xsl:text>", "</xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>", </xsl:text>
			<xsl:value-of select="@name" />
			<xsl:text>);</xsl:text>
		</xsl:for-each>
    <xsl:text>
      log(logger, "</xsl:text>
        <xsl:value-of select="$package_name" />
        <xsl:text>", "</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>", org.znerd.util.log.LogLevel.</xsl:text>
        <xsl:value-of select="@level" />
        <xsl:text>, TRANSLATION_BUNDLE.translation_</xsl:text>
    <xsl:value-of select="@id" />
    <xsl:text>(</xsl:text>
    <xsl:if test="$exception">
      <xsl:text>_exception</xsl:text>
    </xsl:if>
    <xsl:for-each select="param">
      <xsl:if test="$exception or (position() &gt; 1)">
        <xsl:text>, </xsl:text>
      </xsl:if>
      <xsl:value-of select="@name" />
    </xsl:for-each>
    <xsl:text>), </xsl:text> 

        <xsl:choose>
          <xsl:when test="$exception">
            <xsl:text>_exception</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>(java.lang.Throwable) null</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text>);
   }</xsl:text>
	</xsl:template>
</xsl:stylesheet>
