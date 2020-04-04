package b4a.example;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "b4a.example", "b4a.example.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.phone.PhoneEvents _pe = null;
public static anywheresoftware.b4a.phone.PhoneEvents.SMSInterceptor _si = null;
public b4a.example.main _main = null;
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 34;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return false;
}
public static String  _pe_smssentstatus(boolean _success,String _errormessage,String _phonenumber,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Sub PE_SmsSentStatus (Success As Boolean, ErrorMes";
 //BA.debugLineNum = 96;BA.debugLine="Log(\"SmsSentStatus Success = \" & Success & \", Err";
anywheresoftware.b4a.keywords.Common.LogImpl("22031617","SmsSentStatus Success = "+BA.ObjectToString(_success)+", ErrorMessage = "+_errormessage,0);
 //BA.debugLineNum = 97;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 98;BA.debugLine="Main.TTS.Speak(\"Recarga Enviada\",False)";
mostCurrent._main._tts /*anywheresoftware.b4a.obejcts.TTS*/ .Speak("Recarga Enviada",anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 100;BA.debugLine="Main.TTS.Speak(\"Recarga Fallida\",False)";
mostCurrent._main._tts /*anywheresoftware.b4a.obejcts.TTS*/ .Speak("Recarga Fallida",anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 103;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 104;BA.debugLine="CallSub(Main,\"EnablebtnSend\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"EnablebtnSend");
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static String  _pe_texttospeechfinish(anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub PE_TextToSpeechFinish (Intent As Intent)";
 //BA.debugLineNum = 53;BA.debugLine="Log(\"TextToSpeechFinish\")";
anywheresoftware.b4a.keywords.Common.LogImpl("2917505","TextToSpeechFinish",0);
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _pe_userpresent(anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub PE_UserPresent (Intent As Intent)";
 //BA.debugLineNum = 88;BA.debugLine="Log(\"UserPresent\")";
anywheresoftware.b4a.keywords.Common.LogImpl("21572865","UserPresent",0);
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim PE As PhoneEvents";
_pe = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 10;BA.debugLine="Dim SI As SmsInterceptor";
_si = new anywheresoftware.b4a.phone.PhoneEvents.SMSInterceptor();
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 18;BA.debugLine="PE.Initialize(\"PE\")";
_pe.Initialize(processBA,"PE");
 //BA.debugLineNum = 19;BA.debugLine="SI.Initialize2(\"SI\",999)";
_si.Initialize2("SI",processBA,(int) (999));
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 25;BA.debugLine="Service.StopAutomaticForeground 'Starter service";
mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static boolean  _si_messagereceived(String _from,String _body) throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub SI_MessageReceived (From As String, Body As St";
 //BA.debugLineNum = 43;BA.debugLine="Log(\"MessageReceived: From = \" & From & \", Body =";
anywheresoftware.b4a.keywords.Common.LogImpl("2851970","MessageReceived: From = "+_from+", Body = "+_body,0);
 //BA.debugLineNum = 45;BA.debugLine="If From=\"2662\" or From=\"2661\" Then";
if ((_from).equals("2662") || (_from).equals("2661")) { 
 //BA.debugLineNum = 46;BA.debugLine="CallSub2(Main,\"ShowMensaje\",Body)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"ShowMensaje",(Object)(_body));
 };
 //BA.debugLineNum = 49;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return false;
}
}
