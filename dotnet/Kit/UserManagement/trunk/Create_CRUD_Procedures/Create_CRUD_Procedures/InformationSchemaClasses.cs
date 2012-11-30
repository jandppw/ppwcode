namespace Create_CRUD_Procedures {

    public partial class RelationInfo {

        public string GetDBType() {
            string DBType;

            switch (DATA_TYPE.ToLower()) {
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                    if (CHARACTER_MAXIMUM_LENGTH == -1) {
                        return string.Concat(DATA_TYPE, "(max)");
                    }
                    else {
                        return string.Concat(DATA_TYPE, '(', CHARACTER_MAXIMUM_LENGTH, ')');
                    }
                case "decimal":
                case "numeric":
                    return string.Concat(DATA_TYPE, '(', NUMERIC_PRECISION, ',', NUMERIC_SCALE, ')');
                case "varbinary":
                    if (CHARACTER_OCTET_LENGTH == -1) {
                        return string.Concat(DATA_TYPE, "(max)");
                    }
                    else {
                        return string.Concat(DATA_TYPE, '(', CHARACTER_OCTET_LENGTH, ')');
                    }
                default:
                    DBType = DATA_TYPE;
                    break;
            }
            return DBType;
        }
    }
}
