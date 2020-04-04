B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=9.01
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim PE As PhoneEvents
	Dim SI As SmsInterceptor
	
	
End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	PE.Initialize("PE")
	SI.Initialize2("SI",999)
	
	
End Sub

Sub Service_Start (StartingIntent As Intent)
	Service.StopAutomaticForeground 'Starter service can start in the foreground state in some edge cases.
End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub

Sub SI_MessageReceived (From As String, Body As String) As Boolean
	
	Log("MessageReceived: From = " & From & ", Body = " & Body)
	
	If From="2662" or From="2661" Then
			CallSub2(Main,"ShowMensaje",Body)
	End If
	
	Return True
End Sub

Sub PE_TextToSpeechFinish (Intent As Intent)
	Log("TextToSpeechFinish")
End Sub
'Sub PE_ConnectivityChanged (NetworkType As String, State As String, Intent As Intent)
'	Log("ConnectivityChanged: " & NetworkType & ", state = " & State)
'	Log(Intent.ExtrasToString)
'End Sub
'Sub PE_AirplaneModeChanged (State As Boolean, Intent As Intent)
'	Log("AirplaneModeChanged: "& State)
'	Log(Intent.ExtrasToString)
'End Sub
'Sub PE_BatteryChanged (Level As Int, Scale As Int, Plugged As Boolean, Intent As Intent)
'	Log("BatteryChanged: Level = " & Level & ", Scale = " & Scale & ", Plugged = " & Plugged)
'End Sub
'Sub PE_CameraButtonPressed (Intent As Intent)
'	Log("CameraButtonPressed")
'	Log(Intent.ExtrasToString)
'End Sub
'Sub PE_PackageRemoved (Package As String, Intent As Intent)
'	Log("PackageRemoved: " & Package)
'	Log(Intent.ExtrasToString)
'End Sub
'Sub PE_PackageAdded (Package As String, Intent As Intent)
'	Log("PackageAdded: " & Package)
'	Log(Intent.ExtrasToString)
'End Sub
'Sub PE_ScreenOff (Intent As Intent)
'	Log("ScreenOff")
'End Sub
'Sub PE_ScreenOn (Intent As Intent)
'	Log("ScreenOn")
'End Sub
'Sub PE_Shutdown (Intent As Intent)
'	Log("Shutdown")
'End Sub
Sub PE_UserPresent (Intent As Intent)
	Log("UserPresent")
End Sub
'Sub PE_PhoneStateChanged (State As String, IncomingNumber As String, Intent As Intent)
'	Log("PhoneStateChanged, State = " & State & ", IncomingNumber = " & IncomingNumber)
'	Log(Intent.ExtrasToString)
'End Sub

Sub PE_SmsSentStatus (Success As Boolean, ErrorMessage As String, PhoneNumber As String, Intent As Intent)
	Log("SmsSentStatus Success = " & Success & ", ErrorMessage = " & ErrorMessage)
	If Success Then 
		Main.TTS.Speak("Recarga Enviada",False)
	Else
		Main.TTS.Speak("Recarga Fallida",False)
	End If
	
	ProgressDialogHide
	CallSub(Main,"EnablebtnSend")
End Sub


