<?xml version="1.0" encoding="UTF-8" ?>
<!-- See the COPYRIGHT file for redistribution and use restrictions. -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="param" mode="methodArgument">
    <xsl:param name="exception" select="false()" />
    <xsl:param name="comma"     select="false()" />

    <xsl:variable name="nullable" select="not(@nullable) or @nullable = 'true'" />

    <xsl:if test="starts-with(@name, '_')">
      <xsl:message terminate="yes">
        <xsl:text>Parameter name "</xsl:text>
        <xsl:value-of select="@name" />
        <xsl:text>" starts with underscore.</xsl:text>
      </xsl:message>
    </xsl:if>

    <xsl:if test="$comma or $exception or (position() &gt; 1)">
      <xsl:text>, </xsl:text>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="(@type = 'text') or (string-length(@type) &lt; 1)">String</xsl:when>
      <xsl:when test="@type = 'serializable'">org.znerd.logdoc.LogdocSerializable</xsl:when>
      <xsl:when test="@type = 'object'">java.lang.Object</xsl:when>
      <xsl:when test="(@type = 'float64') and $nullable">java.lang.Double</xsl:when>
      <xsl:when test="@type = 'float64'">double</xsl:when>
      <xsl:when test="(@type = 'float32') and $nullable">java.lang.Float</xsl:when>
      <xsl:when test="@type = 'float32'">float</xsl:when>
      <xsl:when test="(@type = 'int64') and $nullable">java.lang.Long</xsl:when>
      <xsl:when test="@type = 'int64'">long</xsl:when>
      <xsl:when test="(@type = 'int32') and $nullable">java.lang.Integer</xsl:when>
      <xsl:when test="@type = 'int32'">int</xsl:when>
      <xsl:when test="(@type = 'int16') and $nullable">java.lang.Short</xsl:when>
      <xsl:when test="@type = 'int16'">short</xsl:when>
      <xsl:when test="(@type = 'int8') and $nullable">java.lang.Byte</xsl:when>
      <xsl:when test="@type = 'int8'">byte</xsl:when>
      <xsl:when test="(@type = 'boolean') and $nullable">java.lang.Boolean</xsl:when>
      <xsl:when test="@type = 'boolean'">boolean</xsl:when>
      <xsl:otherwise>
        <xsl:message terminate="yes">
          <xsl:text>The type '</xsl:text>
          <xsl:value-of select="@type" />
          <xsl:text>' for parameter named '</xsl:text>
          <xsl:value-of select="@name" />
          <xsl:text>' is  unknown.</xsl:text>
        </xsl:message>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text> </xsl:text>
    <xsl:value-of select="@name" />
  </xsl:template>
</xsl:stylesheet>
