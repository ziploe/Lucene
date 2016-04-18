package org.ziploe.test;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;

public final class MyTextField extends Field {

	  /** Indexed, tokenized, not stored. */
	  public static final FieldType TYPE_NOT_STORED = new FieldType();

	  /** Indexed, tokenized, stored. */
	  public static final FieldType TYPE_STORED = new FieldType();
	 // DocValuesType dvt = (DocValuesType) "SORTED";

	  static {
	    TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    TYPE_NOT_STORED.setTokenized(true);
	   // TYPE_NOT_STORED.setDocValuesType(DocValuesType.SORTED);
	    //TYPE_STORED.setStored(true);
	    TYPE_NOT_STORED.freeze();

	    TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    TYPE_STORED.setTokenized(true);
	    TYPE_STORED.setStored(true);
	    TYPE_STORED.freeze();
	  }

	  // TODO: add sugar for term vectors...?

	  /** Creates a new un-stored MyTextField with Reader value. 
	   * @param name field name
	   * @param reader reader value
	   * @throws IllegalArgumentException if the field name is null
	   * @throws NullPointerException if the reader is null
	   */
	  public MyTextField(String name, Reader reader) {
	   // super(name, reader, TYPE_NOT_STORED);
		  super(name, reader, TYPE_NOT_STORED);
	  }

	  /** Creates a new MyTextField with String value. 
	   * @param name field name
	   * @param value string value
	   * @param store Store.YES if the content should also be stored
	   * @throws IllegalArgumentException if the field name or value is null.
	   */
	  public MyTextField(String name, String value, Store store) {
	    super(name, value, store == Store.YES ? TYPE_STORED : TYPE_NOT_STORED);
	  }
	  
	  /** Creates a new un-stored MyTextField with TokenStream value. 
	   * @param name field name
	   * @param stream TokenStream value
	   * @throws IllegalArgumentException if the field name is null.
	   * @throws NullPointerException if the tokenStream is null
	   */
	  public MyTextField(String name, TokenStream stream) {
	    super(name, stream, TYPE_NOT_STORED);
	  }
	}

