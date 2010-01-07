package com.znerd.mylibrary;

/**
 * Translation bundle for the <em>en_US</em> locale.
 *
 * @see Log
 */
public final class TranslationBundle_en_US extends TranslationBundle {

   /**
    * The one and only instance of this class.
    */
   public static final TranslationBundle_en_US SINGLETON = new TranslationBundle_();


   /**
    * Constructor for this class. Intentionally made <code>private</code>,
    * since no instances of this class should be created. Instead, the class
    * functions should be used.
    */
   private TranslationBundle_() {
      super("en_US");
   }

   public String translation_100() {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Initializing transaction system.");
      return buffer.toString();
   }

   public String translation_101() {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Transaction system initialized in ");
      if (duration == null) {
         buffer.append("(null)");
      } else {
         buffer.append(duration);
      }
      buffer.append(" ms.");
      return buffer.toString();
   }

   public String translation_102() {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Starting transaction ");
      if (id == null) {
         buffer.append("(null)");
      } else {
         buffer.append(id);
      }
      buffer.append(" (description: ");
      if (description == null) {
         buffer.append("(null)");
      } else {
         buffer.append('"');
         buffer.append(description);
         buffer.append('"');
      }
      buffer.append(").");
      return buffer.toString();
   }

   public String translation_103() {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Transaction ");
      if (id == null) {
         buffer.append("(null)");
      } else {
         buffer.append(id);
      }
      buffer.append(" fully started.");
      return buffer.toString();
   }
}
