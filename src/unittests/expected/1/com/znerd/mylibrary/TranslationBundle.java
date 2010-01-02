package com.znerd.mylibrary;

/**
 * Translation bundle for log messages.
 *
 * @see Log
 *
 * @since MyLibrary 5.5.3
 */
public abstract class TranslationBundle {

   /**
    * The name of this translation bundle.
    */
   private final String _name;

   /**
    * Constructs a new <code>TranslationBundle</code> subclass instance.
    *
    * @param name
    *    the name of this translation bundle, cannot be <code>null</code>.
    *
    * @throws IllegalArgumentException
    *    if <code>name == null</code>.
    */
   protected TranslationBundle(String name)
   throws IllegalArgumentException {

      // Check preconditions
      if (name == null) {
         throw new IllegalArgumentException("name == null");
      }

      // Store information
      _name = name;
   }

   /**
    * Retrieves the name of this translation bundle.
    *
    * @return
    *    the name of this translation bundle.
    */
   public final String getName() {
      return _name;
   }

   /**
    * Get the translation for the log entry with ID 100, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction system initializing.</em></blockquote>
    */
   public String translation_100() {
      return "Transaction system initializing.";
   }

   /**
    * Get the translation for the log entry with ID 101, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction system initialized.</em></blockquote>
    */
   public String translation_101(long duration) {
      return "Transaction system initialized.";
   }

   /**
    * Get the translation for the log entry with ID 102, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction starting.</em></blockquote>
    */
   public String translation_102(int id, String description) {
      return "Transaction starting.";
   }

   /**
    * Get the translation for the log entry with ID 103, in the log entry group <em>Transactions</em>.
    * The description for this log entry is:
    * <blockquote><em>Transaction fully started.</em></blockquote>
    */
   public String translation_103(int id) {
      return "Transaction fully started.";
   }
}
