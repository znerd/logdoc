package com.znerd.mylibrary;

/**
 * Central logging handler.
 *
 * @since MyLibrary 5.5.3
 */
public class Log extends org.znerd.logdoc.internal.log4j.AbstractLog {

   /**
    * The fully-qualified name for this class.
    */
   private static final String FQCN = "com.znerd.mylibrary.Log";

   /**
    * Controller for this <em>logdoc</em> <code>Log</code> class.
    */
   private static final LogController CONTROLLER;

   /**
    * Associations from name to translation bundle.
    */
   private static final java.util.HashMap<String,TranslationBundle> TRANSLATION_BUNDLES_BY_NAME;

   /**
    * The active translation bundle.
    */
   private static TranslationBundle TRANSLATION_BUNDLE;

   /**
    * Logger for the entry with ID 100.
    */
   private static org.apache.log4j.Logger LOGGER_100;

   /**
    * Logger for the entry with ID 101.
    */
   private static org.apache.log4j.Logger LOGGER_101;

   /**
    * Logger for the entry with ID 102.
    */
   private static org.apache.log4j.Logger LOGGER_102;

   /**
    * Logger for the entry with ID 103.
    */
   private static org.apache.log4j.Logger LOGGER_103;


   /**
    * Initializes this class.
    */
   static {

      // Reference all translation bundles by name
      TRANSLATION_BUNDLES_BY_NAME = new java.util.HashMap<String,TranslationBundle>();
      TRANSLATION_BUNDLES_BY_NAME.put("en_US", TranslationBundle_en_US.SINGLETON);

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
   }

   /**
    * Logs the entry with ID 100, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction system initializing.</em></blockquote>
    */
   public static final void log_100() {
      if (LOGGER_100 == null) {
         LOGGER_100 = org.apache.log4j.Logger.getLogger("com.znerd.mylibrary.transactions.100");
      }
      if (LOGGER_100.isEnabledFor(NOTICE)) {
         String _translation = TRANSLATION_BUNDLE.translation_100();
         LOGGER_100.log(FQCN, NOTICE, _translation, null);
      }
   }

   /**
    * Logs the entry with ID 101, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction system initialized.</em></blockquote>
    */
   public static final void log_101(long duration) {
      if (LOGGER_101 == null) {
         LOGGER_101 = org.apache.log4j.Logger.getLogger("com.znerd.mylibrary.transactions.101");
      }
      if (LOGGER_101.isEnabledFor(NOTICE)) {
         String _translation = TRANSLATION_BUNDLE.translation_101(duration);
         LOGGER_101.log(FQCN, NOTICE, _translation, null);
      }
   }

   /**
    * Logs the entry with ID 102, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction starting.</em></blockquote>
    */
   public static final void log_102(int id, String description) {
      if (LOGGER_102 == null) {
         LOGGER_102 = org.apache.log4j.Logger.getLogger("com.znerd.mylibrary.transactions.102");
      }
      if (LOGGER_102.isEnabledFor(NOTICE)) {
         String _translation = TRANSLATION_BUNDLE.translation_102(id, description);
         LOGGER_102.log(FQCN, NOTICE, _translation, null);
      }
   }

   /**
    * Logs the entry with ID 103, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction fully started.</em></blockquote>
    */
   public static final void log_103(int id) {
      if (LOGGER_103 == null) {
         LOGGER_103 = org.apache.log4j.Logger.getLogger("com.znerd.mylibrary.transactions.103");
      }
      if (LOGGER_103.isEnabledFor(INFO)) {
         String _translation = TRANSLATION_BUNDLE.translation_103(id);
         LOGGER_103.log(FQCN, INFO, _translation, null);
      }
   }

   /**
    * Controller for this <code>Log</code> class.
    */
   private static final class Controller extends LogController {

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
      protected boolean isLocaleSupported(String locale) {

         // Return true if the bundle exists
         return TRANSLATION_BUNDLES_BY_NAME.containsKey(locale);
      }

	  @Override
      protected void setLocale(String newLocale) {
         TRANSLATION_BUNDLE = TRANSLATION_BUNDLES_BY_NAME.get(newLocale);
      }
   }
}
