using System.Collections.Generic;

namespace PPWCode.Kit.Tasks.Server.API_I
{
    public class TaskQuery
    {
        public TaskQuery(string queryString, IDictionary<string, object> parameters)
        {
            QueryString = queryString;
            Parameters = parameters;
        }

        public string QueryString { get; private set; }
        public IDictionary<string, object> Parameters { get; private set; }
    }
}