/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.kgk.debezium.engine.demo.dto;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@org.apache.avro.specific.AvroGenerated
public class Source extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7691303217414650370L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Source\",\"namespace\":\"com.kgk.debezium.engine.demo.dto\",\"fields\":[{\"name\":\"db\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"schema\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"table\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"lsn\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"ts_ms\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Source> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Source> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Source> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Source> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Source> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Source to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Source from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Source instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Source fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.String db;
  private java.lang.String schema;
  private java.lang.String table;
  private java.lang.Long lsn;
  private long ts_ms;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Source() {}

  /**
   * All-args constructor.
   * @param db The new value for db
   * @param schema The new value for schema
   * @param table The new value for table
   * @param lsn The new value for lsn
   * @param ts_ms The new value for ts_ms
   */
  public Source(java.lang.String db, java.lang.String schema, java.lang.String table, java.lang.Long lsn, java.lang.Long ts_ms) {
    this.db = db;
    this.schema = schema;
    this.table = table;
    this.lsn = lsn;
    this.ts_ms = ts_ms;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return db;
    case 1: return schema;
    case 2: return table;
    case 3: return lsn;
    case 4: return ts_ms;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: db = value$ != null ? value$.toString() : null; break;
    case 1: schema = value$ != null ? value$.toString() : null; break;
    case 2: table = value$ != null ? value$.toString() : null; break;
    case 3: lsn = (java.lang.Long)value$; break;
    case 4: ts_ms = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'db' field.
   * @return The value of the 'db' field.
   */
  public java.lang.String getDb() {
    return db;
  }


  /**
   * Sets the value of the 'db' field.
   * @param value the value to set.
   */
  public void setDb(java.lang.String value) {
    this.db = value;
  }

  /**
   * Gets the value of the 'schema' field.
   * @return The value of the 'schema' field.
   */
  public java.lang.String getSchema$() {
    return schema;
  }


  /**
   * Sets the value of the 'schema' field.
   * @param value the value to set.
   */
  public void setSchema$(java.lang.String value) {
    this.schema = value;
  }

  /**
   * Gets the value of the 'table' field.
   * @return The value of the 'table' field.
   */
  public java.lang.String getTable() {
    return table;
  }


  /**
   * Sets the value of the 'table' field.
   * @param value the value to set.
   */
  public void setTable(java.lang.String value) {
    this.table = value;
  }

  /**
   * Gets the value of the 'lsn' field.
   * @return The value of the 'lsn' field.
   */
  public java.lang.Long getLsn() {
    return lsn;
  }


  /**
   * Sets the value of the 'lsn' field.
   * @param value the value to set.
   */
  public void setLsn(java.lang.Long value) {
    this.lsn = value;
  }

  /**
   * Gets the value of the 'ts_ms' field.
   * @return The value of the 'ts_ms' field.
   */
  public long getTsMs() {
    return ts_ms;
  }


  /**
   * Sets the value of the 'ts_ms' field.
   * @param value the value to set.
   */
  public void setTsMs(long value) {
    this.ts_ms = value;
  }

  /**
   * Creates a new Source RecordBuilder.
   * @return A new Source RecordBuilder
   */
  public static com.kgk.debezium.engine.demo.dto.Source.Builder newBuilder() {
    return new com.kgk.debezium.engine.demo.dto.Source.Builder();
  }

  /**
   * Creates a new Source RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Source RecordBuilder
   */
  public static com.kgk.debezium.engine.demo.dto.Source.Builder newBuilder(com.kgk.debezium.engine.demo.dto.Source.Builder other) {
    if (other == null) {
      return new com.kgk.debezium.engine.demo.dto.Source.Builder();
    } else {
      return new com.kgk.debezium.engine.demo.dto.Source.Builder(other);
    }
  }

  /**
   * Creates a new Source RecordBuilder by copying an existing Source instance.
   * @param other The existing instance to copy.
   * @return A new Source RecordBuilder
   */
  public static com.kgk.debezium.engine.demo.dto.Source.Builder newBuilder(com.kgk.debezium.engine.demo.dto.Source other) {
    if (other == null) {
      return new com.kgk.debezium.engine.demo.dto.Source.Builder();
    } else {
      return new com.kgk.debezium.engine.demo.dto.Source.Builder(other);
    }
  }

  /**
   * RecordBuilder for Source instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Source>
    implements org.apache.avro.data.RecordBuilder<Source> {

    private java.lang.String db;
    private java.lang.String schema;
    private java.lang.String table;
    private java.lang.Long lsn;
    private long ts_ms;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.kgk.debezium.engine.demo.dto.Source.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.db)) {
        this.db = data().deepCopy(fields()[0].schema(), other.db);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.schema)) {
        this.schema = data().deepCopy(fields()[1].schema(), other.schema);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.table)) {
        this.table = data().deepCopy(fields()[2].schema(), other.table);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.lsn)) {
        this.lsn = data().deepCopy(fields()[3].schema(), other.lsn);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.ts_ms)) {
        this.ts_ms = data().deepCopy(fields()[4].schema(), other.ts_ms);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing Source instance
     * @param other The existing instance to copy.
     */
    private Builder(com.kgk.debezium.engine.demo.dto.Source other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.db)) {
        this.db = data().deepCopy(fields()[0].schema(), other.db);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.schema)) {
        this.schema = data().deepCopy(fields()[1].schema(), other.schema);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.table)) {
        this.table = data().deepCopy(fields()[2].schema(), other.table);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.lsn)) {
        this.lsn = data().deepCopy(fields()[3].schema(), other.lsn);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.ts_ms)) {
        this.ts_ms = data().deepCopy(fields()[4].schema(), other.ts_ms);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'db' field.
      * @return The value.
      */
    public java.lang.String getDb() {
      return db;
    }


    /**
      * Sets the value of the 'db' field.
      * @param value The value of 'db'.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder setDb(java.lang.String value) {
      validate(fields()[0], value);
      this.db = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'db' field has been set.
      * @return True if the 'db' field has been set, false otherwise.
      */
    public boolean hasDb() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'db' field.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder clearDb() {
      db = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'schema' field.
      * @return The value.
      */
    public java.lang.String getSchema$() {
      return schema;
    }


    /**
      * Sets the value of the 'schema' field.
      * @param value The value of 'schema'.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder setSchema$(java.lang.String value) {
      validate(fields()[1], value);
      this.schema = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'schema' field has been set.
      * @return True if the 'schema' field has been set, false otherwise.
      */
    public boolean hasSchema$() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'schema' field.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder clearSchema$() {
      schema = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'table' field.
      * @return The value.
      */
    public java.lang.String getTable() {
      return table;
    }


    /**
      * Sets the value of the 'table' field.
      * @param value The value of 'table'.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder setTable(java.lang.String value) {
      validate(fields()[2], value);
      this.table = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'table' field has been set.
      * @return True if the 'table' field has been set, false otherwise.
      */
    public boolean hasTable() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'table' field.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder clearTable() {
      table = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'lsn' field.
      * @return The value.
      */
    public java.lang.Long getLsn() {
      return lsn;
    }


    /**
      * Sets the value of the 'lsn' field.
      * @param value The value of 'lsn'.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder setLsn(java.lang.Long value) {
      validate(fields()[3], value);
      this.lsn = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'lsn' field has been set.
      * @return True if the 'lsn' field has been set, false otherwise.
      */
    public boolean hasLsn() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'lsn' field.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder clearLsn() {
      lsn = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'ts_ms' field.
      * @return The value.
      */
    public long getTsMs() {
      return ts_ms;
    }


    /**
      * Sets the value of the 'ts_ms' field.
      * @param value The value of 'ts_ms'.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder setTsMs(long value) {
      validate(fields()[4], value);
      this.ts_ms = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'ts_ms' field has been set.
      * @return True if the 'ts_ms' field has been set, false otherwise.
      */
    public boolean hasTsMs() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'ts_ms' field.
      * @return This builder.
      */
    public com.kgk.debezium.engine.demo.dto.Source.Builder clearTsMs() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Source build() {
      try {
        Source record = new Source();
        record.db = fieldSetFlags()[0] ? this.db : (java.lang.String) defaultValue(fields()[0]);
        record.schema = fieldSetFlags()[1] ? this.schema : (java.lang.String) defaultValue(fields()[1]);
        record.table = fieldSetFlags()[2] ? this.table : (java.lang.String) defaultValue(fields()[2]);
        record.lsn = fieldSetFlags()[3] ? this.lsn : (java.lang.Long) defaultValue(fields()[3]);
        record.ts_ms = fieldSetFlags()[4] ? this.ts_ms : (java.lang.Long) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Source>
    WRITER$ = (org.apache.avro.io.DatumWriter<Source>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Source>
    READER$ = (org.apache.avro.io.DatumReader<Source>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.db);

    out.writeString(this.schema);

    out.writeString(this.table);

    if (this.lsn == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeLong(this.lsn);
    }

    out.writeLong(this.ts_ms);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.db = in.readString();

      this.schema = in.readString();

      this.table = in.readString();

      if (in.readIndex() != 1) {
        in.readNull();
        this.lsn = null;
      } else {
        this.lsn = in.readLong();
      }

      this.ts_ms = in.readLong();

    } else {
      for (int i = 0; i < 5; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.db = in.readString();
          break;

        case 1:
          this.schema = in.readString();
          break;

        case 2:
          this.table = in.readString();
          break;

        case 3:
          if (in.readIndex() != 1) {
            in.readNull();
            this.lsn = null;
          } else {
            this.lsn = in.readLong();
          }
          break;

        case 4:
          this.ts_ms = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










