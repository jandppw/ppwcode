using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace PPWCode.Util.Quartz
{
    [Serializable]
    public sealed class AggregateException : 
        Exception
    {
         #region Constructors

        public AggregateException()
            : base(null, null)
        {
            Set = new HashSet<Exception>();
        }

        public AggregateException(string message)
            : base(message)
        {
            Set = new HashSet<Exception>();
        }

        public AggregateException(string message, IEnumerable<Exception> exceptions)
            : base(message)
        {
            Set = (HashSet<Exception>)exceptions;
        }

        private AggregateException(SerializationInfo info, StreamingContext context)
            : base(info, context)
        {
        }

        #endregion

        #region Properties
     
        private HashSet<Exception> Set
        {
            get
            {
                return Data["Set"] as HashSet<Exception>;
            }
            set
            {
                Data["Set"] = value;
            }
        }

        public ICollection<Exception> Elements
        {
            get { return Set.ToArray(); }
        }

        #endregion

        public void AddElement(Exception exception)
        {
            AggregateException cse = exception as AggregateException;
            if (cse == null)
            {
                Set.Add(exception);
            }
            else
            {
                foreach (Exception ex in cse.Elements)
                {
                    AddElement(ex);
                }
            }
        }

        public override string ToString()
        {
            try
            {
                StringBuilder sb = new StringBuilder(1024);
                foreach (Exception se in Set)
                {
                    sb.AppendLine(se.ToString());
                }
                return sb.Length == 0 ? Message : sb.ToString();
            }
            catch
            {
                return Message;
            }
        }
    }
}
