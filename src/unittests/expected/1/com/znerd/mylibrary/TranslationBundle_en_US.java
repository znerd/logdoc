package com.znerd.mylibrary;

/**
 * Translation bundle for the <em>en_US</em> locale.
 *
 * @see Log
 *
 * @since MyLibrary 5.5.3
 */
public final class TranslationBundle_en_US extends TranslationBundle {

   /**
    * The one and only instance of this class.
    */
   public static final TranslationBundle_en_US SINGLETON = new TranslationBundle_en_US();


   /**
    * Constructor for this class. Intentionally made <code>private</code>,
    * since no instances of this class should be created. Instead, the class
    * functions should be used.
    */
   private TranslationBundle_en_US() {
      super("en_US");
   }

   public String translation_100() {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Initializing transaction system.");
      return buffer.toString();
   }

   public String translation_101(long duration) {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Transaction system initialized in ");
      buffer.append(duration);
      buffer.append(" ms.");
      return buffer.toString();
   }

   public String translation_102(int id, String description) {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Starting transaction ");
      buffer.append(id);
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

   public String translation_103(int id) {
      StringBuffer buffer = new StringBuffer(255);
      buffer.append("Transaction ");
      buffer.append(id);
      buffer.append(" fully started.");
      return buffer.toString();
   }
}
