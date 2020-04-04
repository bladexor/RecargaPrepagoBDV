package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.obejcts.TTS _tts = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnsendsms = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spservicio = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spmonto = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txttelefono = null;
public anywheresoftware.b4a.objects.collections.List _montosdigitel = null;
public anywheresoftware.b4a.objects.collections.List _montosmovilnet = null;
public anywheresoftware.b4a.objects.collections.List _montosmovistar = null;
public b4a.example.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 39;BA.debugLine="StartService(Starter)";
anywheresoftware.b4a.keywords.Common.StartService(processBA,(Object)(mostCurrent._starter.getObject()));
 //BA.debugLineNum = 40;BA.debugLine="Activity.LoadLayout(\"home\")";
mostCurrent._activity.LoadLayout("home",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="TTS.Initialize(\"tts\")";
_tts.Initialize(processBA,"tts");
 //BA.debugLineNum = 43;BA.debugLine="spServicio.Add(\"Seleccione Servicio...\")";
mostCurrent._spservicio.Add("Seleccione Servicio...");
 //BA.debugLineNum = 44;BA.debugLine="spServicio.Add(\"Digitel\")";
mostCurrent._spservicio.Add("Digitel");
 //BA.debugLineNum = 45;BA.debugLine="spServicio.Add(\"Movilnet\")";
mostCurrent._spservicio.Add("Movilnet");
 //BA.debugLineNum = 46;BA.debugLine="spServicio.Add(\"MOVISTARPREPAGO\")";
mostCurrent._spservicio.Add("MOVISTARPREPAGO");
 //BA.debugLineNum = 48;BA.debugLine="montosDigitel.Initialize";
mostCurrent._montosdigitel.Initialize();
 //BA.debugLineNum = 49;BA.debugLine="montosMovilnet.Initialize";
mostCurrent._montosmovilnet.Initialize();
 //BA.debugLineNum = 50;BA.debugLine="montosMovistar.Initialize";
mostCurrent._montosmovistar.Initialize();
 //BA.debugLineNum = 52;BA.debugLine="montosDigitel.Add(\"20.000\")";
mostCurrent._montosdigitel.Add((Object)("20.000"));
 //BA.debugLineNum = 53;BA.debugLine="montosDigitel.Add(\"60.000\")";
mostCurrent._montosdigitel.Add((Object)("60.000"));
 //BA.debugLineNum = 54;BA.debugLine="montosDigitel.Add(\"100.000\")";
mostCurrent._montosdigitel.Add((Object)("100.000"));
 //BA.debugLineNum = 55;BA.debugLine="montosDigitel.Add(\"200.000\")";
mostCurrent._montosdigitel.Add((Object)("200.000"));
 //BA.debugLineNum = 56;BA.debugLine="montosDigitel.Add(\"400.000\")";
mostCurrent._montosdigitel.Add((Object)("400.000"));
 //BA.debugLineNum = 58;BA.debugLine="montosMovilnet.Add(\"20.000\")";
mostCurrent._montosmovilnet.Add((Object)("20.000"));
 //BA.debugLineNum = 59;BA.debugLine="montosMovilnet.Add(\"30.000\")";
mostCurrent._montosmovilnet.Add((Object)("30.000"));
 //BA.debugLineNum = 60;BA.debugLine="montosMovilnet.Add(\"50.000\")";
mostCurrent._montosmovilnet.Add((Object)("50.000"));
 //BA.debugLineNum = 61;BA.debugLine="montosMovilnet.Add(\"100.000\")";
mostCurrent._montosmovilnet.Add((Object)("100.000"));
 //BA.debugLineNum = 62;BA.debugLine="montosMovilnet.Add(\"200.000\")";
mostCurrent._montosmovilnet.Add((Object)("200.000"));
 //BA.debugLineNum = 64;BA.debugLine="montosMovistar.Add(\"40.000\")";
mostCurrent._montosmovistar.Add((Object)("40.000"));
 //BA.debugLineNum = 65;BA.debugLine="montosMovistar.Add(\"50.000\")";
mostCurrent._montosmovistar.Add((Object)("50.000"));
 //BA.debugLineNum = 66;BA.debugLine="montosMovistar.Add(\"80.000\")";
mostCurrent._montosmovistar.Add((Object)("80.000"));
 //BA.debugLineNum = 67;BA.debugLine="montosMovistar.Add(\"200.000\")";
mostCurrent._montosmovistar.Add((Object)("200.000"));
 //BA.debugLineNum = 68;BA.debugLine="montosMovistar.Add(\"400.000\")";
mostCurrent._montosmovistar.Add((Object)("400.000"));
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 82;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _btnsendsms_click() throws Exception{
anywheresoftware.b4a.phone.Phone.PhoneSms _sms1 = null;
String _smstext = "";
int _result = 0;
 //BA.debugLineNum = 85;BA.debugLine="Sub btnSendSMS_Click";
 //BA.debugLineNum = 86;BA.debugLine="Dim sms1 As PhoneSms";
_sms1 = new anywheresoftware.b4a.phone.Phone.PhoneSms();
 //BA.debugLineNum = 88;BA.debugLine="Dim smsText As String";
_smstext = "";
 //BA.debugLineNum = 90;BA.debugLine="smsText=\"Servicio \" & spServicio.SelectedItem & \"";
_smstext = "Servicio "+mostCurrent._spservicio.getSelectedItem()+" "+mostCurrent._txttelefono.getText()+" "+mostCurrent._spmonto.getSelectedItem().replace(".","")+",00";
 //BA.debugLineNum = 92;BA.debugLine="Dim result As Int=Msgbox2(\"Recarga \" & spServicio";
_result = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Recarga "+mostCurrent._spservicio.getSelectedItem()+anywheresoftware.b4a.keywords.Common.CRLF+"Monto: "+mostCurrent._spmonto.getSelectedItem()+" Bs"+anywheresoftware.b4a.keywords.Common.CRLF+"Telefono: "+mostCurrent._txttelefono.getText()),BA.ObjectToCharSequence("Confirmar"),"Recargar","Cancelar","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 94;BA.debugLine="If result=DialogResponse.CANCEL Then";
if (_result==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 95;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 98;BA.debugLine="sms1.Send(\"2661\",smsText)";
_sms1.Send("2661",_smstext);
 //BA.debugLineNum = 100;BA.debugLine="spServicio.Enabled=False";
mostCurrent._spservicio.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 101;BA.debugLine="spMonto.Enabled=False";
mostCurrent._spmonto.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 102;BA.debugLine="txtTelefono.Enabled=False";
mostCurrent._txttelefono.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 103;BA.debugLine="btnSendSMS.Enabled=False";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 105;BA.debugLine="ProgressDialogShow2(\"Enviando SMS: \" & CRLF &  sm";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Enviando SMS: "+anywheresoftware.b4a.keywords.Common.CRLF+_smstext),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _enablebtnsend() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Public Sub EnablebtnSend()";
 //BA.debugLineNum = 145;BA.debugLine="spServicio.Enabled=True";
mostCurrent._spservicio.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 146;BA.debugLine="spMonto.Enabled=True";
mostCurrent._spmonto.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="txtTelefono.Enabled=True";
mostCurrent._txttelefono.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 148;BA.debugLine="btnSendSMS.Enabled=True";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private btnSendSMS As Button";
mostCurrent._btnsendsms = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private spServicio As Spinner";
mostCurrent._spservicio = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private spMonto As Spinner";
mostCurrent._spmonto = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private txtTelefono As EditText";
mostCurrent._txttelefono = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private montosDigitel As List";
mostCurrent._montosdigitel = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 32;BA.debugLine="Private montosMovilnet As List";
mostCurrent._montosmovilnet = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 33;BA.debugLine="Private montosMovistar As List";
mostCurrent._montosmovistar = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Public TTS As TTS";
_tts = new anywheresoftware.b4a.obejcts.TTS();
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _showmensaje(String _mensaje) throws Exception{
 //BA.debugLineNum = 152;BA.debugLine="Public Sub ShowMensaje(Mensaje As String)";
 //BA.debugLineNum = 153;BA.debugLine="Msgbox(Mensaje,\"Informacion\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence(_mensaje),BA.ObjectToCharSequence("Informacion"),mostCurrent.activityBA);
 //BA.debugLineNum = 154;BA.debugLine="End Sub";
return "";
}
public static String  _spservicio_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 109;BA.debugLine="Sub spServicio_ItemClick (Position As Int, Value A";
 //BA.debugLineNum = 110;BA.debugLine="spMonto.Clear";
mostCurrent._spmonto.Clear();
 //BA.debugLineNum = 111;BA.debugLine="spMonto.Enabled=True";
mostCurrent._spmonto.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 112;BA.debugLine="spMonto.SelectedIndex=0";
mostCurrent._spmonto.setSelectedIndex((int) (0));
 //BA.debugLineNum = 114;BA.debugLine="txtTelefono.Enabled=True";
mostCurrent._txttelefono.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 117;BA.debugLine="txtTelefono.Text=\"04\"";
mostCurrent._txttelefono.setText(BA.ObjectToCharSequence("04"));
 //BA.debugLineNum = 118;BA.debugLine="txtTelefono.SetSelection(2,0)";
mostCurrent._txttelefono.SetSelection((int) (2),(int) (0));
 //BA.debugLineNum = 119;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Digitel"),(Object)("Movilnet"),(Object)("MOVISTARPREPAGO"))) {
case 0: {
 //BA.debugLineNum = 121;BA.debugLine="spMonto.AddAll(montosDigitel)";
mostCurrent._spmonto.AddAll(mostCurrent._montosdigitel);
 //BA.debugLineNum = 122;BA.debugLine="txtTelefono.Text=\"0412\"";
mostCurrent._txttelefono.setText(BA.ObjectToCharSequence("0412"));
 //BA.debugLineNum = 123;BA.debugLine="txtTelefono.SetSelection(4,0)";
mostCurrent._txttelefono.SetSelection((int) (4),(int) (0));
 break; }
case 1: {
 //BA.debugLineNum = 125;BA.debugLine="spMonto.AddAll(montosMovilnet)";
mostCurrent._spmonto.AddAll(mostCurrent._montosmovilnet);
 break; }
case 2: {
 //BA.debugLineNum = 127;BA.debugLine="spMonto.AddAll(montosMovistar)";
mostCurrent._spmonto.AddAll(mostCurrent._montosmovistar);
 break; }
default: {
 //BA.debugLineNum = 129;BA.debugLine="spMonto.Enabled=False";
mostCurrent._spmonto.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 130;BA.debugLine="txtTelefono.Enabled=False";
mostCurrent._txttelefono.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 131;BA.debugLine="btnSendSMS.Enabled=False";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 break; }
}
;
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return "";
}
public static String  _tts_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub TTS_Ready (Success As Boolean)";
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
public static String  _txttelefono_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 135;BA.debugLine="Sub txtTelefono_TextChanged (Old As String, New As";
 //BA.debugLineNum = 136;BA.debugLine="If New.Length=11 Then";
if (_new.length()==11) { 
 //BA.debugLineNum = 137;BA.debugLine="btnSendSMS.Enabled=True";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 139;BA.debugLine="btnSendSMS.Enabled=False";
mostCurrent._btnsendsms.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
}
