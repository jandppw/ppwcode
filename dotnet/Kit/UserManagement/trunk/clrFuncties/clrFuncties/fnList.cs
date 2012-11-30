using System;
using System.Data;
using Microsoft.SqlServer.Server;
using System.Data.SqlTypes;
using System.IO;
using System.Text;


[Serializable]
[SqlUserDefinedAggregate(
    Format.UserDefined, //use clr serialization to serialize the intermediate result
    IsInvariantToNulls = true, //optimizer property
    IsInvariantToDuplicates = false, //optimizer property
    IsInvariantToOrder = false, //optimizer property
    MaxByteSize = 8000) //maximum size in bytes of persisted value
]
public class fnList : IBinarySerialize
{
    private StringBuilder intermediateResult;

    public void Init()
    {
        intermediateResult = new StringBuilder();
    }

    public void Accumulate(SqlString value)
    {
        if (value.IsNull)
        {
            return;
        }
        intermediateResult.Append(value.Value).Append(',');
    }

    public void Merge(fnList other)
    {
        intermediateResult.Append(other.intermediateResult);
    }

    public SqlString Terminate()
    {
        //delete the trailing comma, if any
        if ((this.intermediateResult != null) && (this.intermediateResult.Length > 0))
        {
            return new SqlString(intermediateResult.ToString(0, this.intermediateResult.Length - 1));
        }

        return SqlString.Null;
    }

    public void Read(BinaryReader r)
    {
        intermediateResult = new StringBuilder(r.ReadString());
    }

    public void Write(BinaryWriter w)
    {
        w.Write(this.intermediateResult.ToString());
    }
}


[Serializable]
[SqlUserDefinedAggregate(
    Format.UserDefined, //use clr serialization to serialize the intermediate result
    IsInvariantToNulls = true, //optimizer property
    IsInvariantToDuplicates = false, //optimizer property
    IsInvariantToOrder = false, //optimizer property
    MaxByteSize = 8000) //maximum size in bytes of persisted value
]
public class fnListEmpty : IBinarySerialize
{
    private StringBuilder intermediateResult;

    public void Init()
    {
        intermediateResult = new StringBuilder();
    }

    public void Accumulate(SqlString value)
    {
        if (value.IsNull)
        {
            return;
        }
        intermediateResult.Append(value.Value);
    }

    public void Merge(fnListEmpty other)
    {
        intermediateResult.Append(other.intermediateResult);
    }

    public SqlString Terminate()
    {
        if (this.intermediateResult != null)
        {
            return new SqlString(intermediateResult.ToString());
        }

        return SqlString.Null;
    }

    public void Read(BinaryReader r)
    {
        intermediateResult = new StringBuilder(r.ReadString());
    }

    public void Write(BinaryWriter w)
    {
        w.Write(this.intermediateResult.ToString());
    }
}


[Serializable]
[SqlUserDefinedAggregate(
    Format.UserDefined, //use clr serialization to serialize the intermediate result
    IsInvariantToNulls = true, //optimizer property
    IsInvariantToDuplicates = false, //optimizer property
    IsInvariantToOrder = false, //optimizer property
    MaxByteSize = 8000) //maximum size in bytes of persisted value
]
public class fnListPipe : IBinarySerialize
{
    private StringBuilder intermediateResult;

    public void Init()
    {
        intermediateResult = new StringBuilder();
    }

    public void Accumulate(SqlString value)
    {
        if (value.IsNull)
        {
            return;
        }
        intermediateResult.Append(value.Value).Append('|');
    }

    public void Merge(fnListPipe other)
    {
        intermediateResult.Append(other.intermediateResult);
    }

    public SqlString Terminate()
    {
        //delete the trailing comma, if any
        if ((this.intermediateResult != null) && (this.intermediateResult.Length > 0))
        {
            return new SqlString(intermediateResult.ToString(0, this.intermediateResult.Length - 1));
        }

        return SqlString.Null;
    }

    public void Read(BinaryReader r)
    {
        intermediateResult = new StringBuilder(r.ReadString());
    }

    public void Write(BinaryWriter w)
    {
        w.Write(this.intermediateResult.ToString());
    }
}
