using System.ComponentModel;

namespace FrontEnd.Components.SelectionComponents
{
	public class MyTypeConverter : EnumConverter
	{

		public MyTypeConverter() : base(typeof(SelectionTypes))
		{

		}
		//public override bool GetStandardValuesSupported(ITypeDescriptorContext context)
		//{
		//    return true;
		//}

		//public override TypeConverter.StandardValuesCollection GetStandardValues(ITypeDescriptorContext context)
		//{
		//    List<String> list = new List<String>();
		//    list.AddRange(Enum.GetNames(typeof(SelectionTypes)));

		//    return new StandardValuesCollection(list);
			
		//}

		//public override bool CanConvertFrom(ITypeDescriptorContext context, Type sourceType)
		//{
		//    return sourceType == typeof(String);
		//}

		//public override object ConvertFrom(ITypeDescriptorContext context, System.Globalization.CultureInfo culture, object value)
		//{
		//    return value;
		//}
	}
}
