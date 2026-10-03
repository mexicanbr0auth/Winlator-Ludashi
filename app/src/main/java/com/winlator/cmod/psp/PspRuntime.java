package com.winlator.cmod.psp;
import android.content.*;
import android.net.Uri;
import android.widget.Toast;
public final class PspRuntime {
 private PspRuntime(){}
 public static void launch(Context c, Uri game) {
   SharedGpuDriver.DriverInfo d=SharedGpuDriver.resolve(c);
   Intent i=new Intent("org.ppsspp.ppsspp.SHORTCUT");
   i.setData(game); i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
   i.putExtra("winlatorDriverId",d.id); i.putExtra("winlatorDriverPath",d.path); i.putExtra("winlatorDriverLibrary",d.library);
   if(i.resolveActivity(c.getPackageManager())!=null) c.startActivity(i);
   else Toast.makeText(c,"Core PPSSPP ainda não instalado nesta build",Toast.LENGTH_LONG).show();
 }
}
