dojo.provide("org.ppwcode.dojo.dijit.period.PeriodForm");

dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.i18n");
dojo.require("dojox.widget.TimeSpinner");
dojo.require("dijit.form.TimeTextBox");
dojo.require("dijit.form.DateTextBox");
dojo.requireLocalization("org.ppwcode.dojo.dijit.period", "PeriodForm");

dojo.declare(
  "org.ppwcode.dojo.dijit.period.PeriodForm",
  [dijit._Widget, dijit._Templated],
  {
    //summary:
    //    PeriodForm is a object to insert date, begin and end time.
    //description:
    //    PeriodForm is a dijit
    //
    templatePath: dojo.moduleUrl("org", "ppwcode/dojo/dijit/period/templates/PeriodForm.html"),

    widgetsInTemplate: true,
    
    //I18N labels
    _dateLabel: null,
    _startDateLabel: null,
    _endDateLabel: null,
    _timeZone: null,
    
    constructor: function() {
	  var localizationbundle = dojo.i18n.getLocalization("org.ppwcode.dojo.dijit.period", "PeriodForm");
	  this._dateLabel = localizationbundle.dateLabel;
	  this._startDateLabel = localizationbundle.startTimeLabel;
	  this._endDateLabel = localizationbundle.endTimeLabel;
	  this._timeZone = localizationbundle.timeZone;
    },
	
    setValue: function(/*BeginEndTimeInterval*/ param){
      this._dateBox.setValue(param.begin);
      this._startTimeBox.setValue(param.begin);
      this._endTimeBox.setValue(param.end);
    },

    getValue: function(){
      var period = new DeterminateIntradayTimeInterval();  
      period.begin = this._addTimeToDate(dojo.clone(this._dateBox.value), this._startTimeBox.value);
      period.end = this._addTimeToDate(dojo.clone(this._dateBox.value), this._endTimeBox.value);
      period.timeZone = this._timeZone;
      return period;
    },

    setAttribute: function(/*String*/ attr, /*anything*/ value){
      this.inherited(arguments);
      switch(attr){
        case "disabled":
          this._dateBox.setAttribute("disabled", value);
          this._startTimeBox.setAttribute("disabled", value);
          this._endTimeBox.setAttribute("disabled", value);
          break;
        default:
          console.log("PeriodForm.setAttribute with " + attr);
      }
    },
    
    _addTimeToDate: function(/*Date*/date, /*Date*/time) {
      date.setHours(0,0,0,0);  
      date = dojo.date.add(date, 'hour', +dojo.date.locale.format(time, {selector: 'time', timePattern: 'HH'}));
      date = dojo.date.add(date, 'minute', +dojo.date.locale.format(time, {selector: 'time', timePattern: 'mm'}));
      date = dojo.date.add(date, 'second', +dojo.date.locale.format(time, {selector: 'time', timePattern: 'ss'}));  
      return date;
    }

  }
);
