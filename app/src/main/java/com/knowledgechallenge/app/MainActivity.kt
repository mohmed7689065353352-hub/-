package com.knowledgechallenge.app
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink=Color(0xFF081522)
private val Panel=Color(0xFF142638)
private val Gold=Color(0xFFE8BD68)
private val Mint=Color(0xFF5DDEC4)
private val Muted=Color(0xFF9FB2C2)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{KnowledgeApp(applicationContext)}}
}
private enum class Mode(val title:String,val subtitle:String,val mark:String){
 DETECTIVE("المحقق الذكي","اجمع الأدلة واكشف الحقيقة","⌕"),RIDDLE("اكشف اللغز","فكّر خارج المألوف","◇"),
 CORRECT("صحّح المعلومة","ميّز الحقيقة من الخطأ","✓"),MEMORY("تحدي الذاكرة","اختبر قوة ملاحظتك","▦"),
 PATTERN("أكمل النمط","اكتشف التسلسل الخفي","⌘"),WRITE("سؤال بلا خيارات","اكتب الإجابة من معرفتك","✎")
}
private data class Challenge(val mode:Mode,val category:String,val prompt:String,val clues:List<String>,val answers:List<String>,val explanation:String)
private val bank=listOf(
 Challenge(Mode.DETECTIVE,"جغرافيا","أنا دولة عربية في شمال أفريقيا، عاصمتي الرباط وأطل على المحيط الأطلسي. من أنا؟",listOf("تقع في أقصى غرب العالم العربي","تشتهر بمدن مثل مراكش وفاس"),listOf("المغرب"),"المغرب دولة عربية تقع في شمال غرب أفريقيا وعاصمتها الرباط."),
 Challenge(Mode.DETECTIVE,"تاريخ","حضارة قديمة بنت الأهرامات الكبرى قرب نهر النيل. ما اسمها؟",listOf("اشتهرت بالفراعنة","من أبرز آثارها أهرامات الجيزة"),listOf("الحضارة المصرية","مصر القديمة"),"الحضارة المصرية القديمة شيدت أهرامات الجيزة."),
 Challenge(Mode.DETECTIVE,"علوم","كوكب أحمر اللون ويُعرف بالكوكب الأحمر. من هو؟",listOf("هو رابع كواكب المجموعة الشمسية","سطحه غني بأكاسيد الحديد"),listOf("المريخ"),"يظهر المريخ بلون مائل إلى الأحمر بسبب أكاسيد الحديد على سطحه."),
 Challenge(Mode.RIDDLE,"ألغاز","شيء كلما أخذت منه كبر، فما هو؟",listOf("لا تراه عادةً كجسم مستقل","يتسع عندما تزيل ما بداخله"),listOf("الحفرة"),"كلما أزلت التراب من الحفرة ازداد حجمها."),
 Challenge(Mode.RIDDLE,"تفكير منطقي","له أسنان ولا يعض، ما هو؟",listOf("تستخدمه لترتيب الشعر","يوجد غالباً في الحمام"),listOf("المشط"),"المشط له أسنان لكنه لا يعض."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي يكتب ولا يقرأ؟",listOf("قد يكون من الخشب أو الحبر","تستخدمه في الدراسة"),listOf("القلم"),"القلم يكتب به الإنسان لكنه لا يقرأ."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: الشمس تدور حول الأرض.",listOf("فكر في مركز المجموعة الشمسية"),listOf("الأرض تدور حول الشمس","تدور الأرض حول الشمس"),"الأرض والكواكب تدور حول الشمس، والأرض تدور أيضاً حول محورها."),
 Challenge(Mode.CORRECT,"جغرافيا","صحّح العبارة: المحيط الهادئ أصغر محيطات العالم.",listOf("قارن مساحته بالمحيط المتجمد الشمالي"),listOf("المحيط الهادئ أكبر محيطات العالم","الهادئ أكبر محيط"),"المحيط الهادئ هو أكبر محيطات العالم مساحةً."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: يتنفس الإنسان باستخدام المعدة.",listOf("عضو التنفس الأساسي زوجي"),listOf("يتنفس الإنسان بالرئتين","الرئتين","الرئة"),"الرئتان هما العضوان الرئيسيان المسؤولان عن تبادل الغازات في التنفس."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ الترتيب: قمر، مفتاح، نخلة، كتاب. ما العنصر الثالث؟",listOf("تخيّل العناصر الأربعة بالترتيب"),listOf("نخلة","النخلة"),"العنصر الثالث في التسلسل هو النخلة."),
 Challenge(Mode.MEMORY,"ذاكرة","تذكّر: أزرق، أصفر، أخضر، أحمر. ما اللون الأول؟",listOf("رتّب الألوان في ذهنك"),listOf("أزرق","الازرق","الأزرق"),"اللون الأول في القائمة هو الأزرق."),
 Challenge(Mode.MEMORY,"ملاحظة","تذكّر الأرقام: 8، 3، 6، 1، 9. ما الرقم الرابع؟",listOf("أعد التسلسل في ذهنك"),listOf("1","١","واحد"),"الرقم الرابع هو 1."),
 Challenge(Mode.PATTERN,"رياضيات","ما العدد التالي؟ 2، 6، 12، 20، 30، ؟",listOf("الفروق: 4، 6، 8، 10"),listOf("42","اثنان واربعون","اثنان وأربعون"),"الفرق التالي 12، إذن العدد هو 42."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل السلسلة: 5، 10، 20، 40، ؟",listOf("كل عدد يساوي ضعف السابق"),listOf("80","ثمانون"),"نضاعف العدد السابق: 40 × 2 = 80."),
 Challenge(Mode.PATTERN,"منطق","ما العدد الناقص؟ 1، 4، 9، 16، ؟",listOf("هذه مربعات أعداد صحيحة متتالية"),listOf("25","خمسة وعشرون"),"الأعداد مربعات: 1²، 2²، 3²، 4²، ثم 5² = 25."),
 Challenge(Mode.WRITE,"علوم","ما اسم العملية التي تصنع بها النباتات غذاءها باستخدام ضوء الشمس؟",listOf("تحدث غالباً في الأوراق وتحتاج إلى الضوء"),listOf("البناء الضوئي","التمثيل الضوئي"),"البناء الضوئي يحول الطاقة الضوئية إلى طاقة كيميائية مخزنة في الغذاء."),
 Challenge(Mode.WRITE,"جغرافيا","ما أكبر قارة في العالم من حيث المساحة؟",listOf("تضم الصين والهند والعديد من الدول"),listOf("آسيا","قارة آسيا"),"آسيا هي أكبر قارات العالم مساحةً."),
 Challenge(Mode.WRITE,"ثقافة عامة","كم عدد أضلاع الشكل السداسي؟",listOf("اسمه يدل على العدد"),listOf("6","٦","ستة"),"الشكل السداسي له ستة أضلاع.")
)
private fun normalize(v:String)=v.trim().lowercase().replace("أ","ا").replace("إ","ا").replace("آ","ا").replace("ى","ي").replace("ة","ه").replace(Regex("[ًٌٍَُِّْـ\\u200f\\u200e]"),"").replace(Regex("\\s+")," ")

@Composable private fun KnowledgeApp(context:Context){
 val prefs=remember{context.getSharedPreferences("knowledge_progress",Context.MODE_PRIVATE)}
 var selected by remember{mutableStateOf<Mode?>(null)}
 var qIndex by remember{mutableIntStateOf(0)}
 var score by remember{mutableIntStateOf(prefs.getInt("score",0))}
 var solved by remember{mutableIntStateOf(prefs.getInt("solved",0))}
 var streak by remember{mutableIntStateOf(prefs.getInt("streak",0))}
 val questions=bank.filter{it.mode==selected}
 val current=questions.getOrNull(qIndex%questions.size.coerceAtLeast(1))
 CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl){
  MaterialTheme(colorScheme=darkColorScheme(background=Ink,surface=Panel,primary=Gold,secondary=Mint,onBackground=Color.White,onSurface=Color.White)){
   Surface(Modifier.fillMaxSize(),color=Ink){
    if(selected==null||current==null) HomeScreen(score,solved,streak){selected=it;qIndex=0}
    else ChallengeScreen(current,qIndex+1,questions.size,{selected=null}){ok->
     if(ok){score+=10;solved++;streak++;prefs.edit().putInt("score",score).putInt("solved",solved).putInt("streak",streak).apply()}
     else{streak=0;prefs.edit().putInt("streak",0).apply()}
     qIndex=(qIndex+1)%questions.size
    }
   }
  }
 }
}
@Composable private fun HomeScreen(score:Int,solved:Int,streak:Int,onStart:(Mode)->Unit){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)){
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){
   Column{Text("تحدي المعرفة",color=Gold,fontSize=28.sp,fontWeight=FontWeight.ExtraBold);Text("فكّر، اكتشف، وتحدَّ نفسك",color=Muted,fontSize=13.sp)}
   Text("✦",color=Gold,fontSize=36.sp)
  }
  Spacer(Modifier.height(22.dp))
  Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF244858),Color(0xFF172536))),RoundedCornerShape(24.dp)).padding(20.dp)){
   Text("مرحباً بك في ساحة التحدي",color=Gold,fontSize=13.sp);Spacer(Modifier.height(7.dp))
   Text("المعرفة ليست حفظاً…\\nإنها طريقة تفكير.",color=Color.White,fontSize=24.sp,fontWeight=FontWeight.Bold,lineHeight=34.sp)
   Spacer(Modifier.height(20.dp))
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Stat("النقاط",score.toString());Stat("تم حلها",solved.toString());Stat("متتالية",streak.toString())}
  }
  Spacer(Modifier.height(25.dp));Text("اختر نمط التحدي",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold)
  Text("أسئلة متنوعة لاختبار مهارات مختلفة",color=Muted,fontSize=12.sp);Spacer(Modifier.height(15.dp))
  Mode.values().toList().chunked(2).forEach{group->
   Row(horizontalArrangement=Arrangement.spacedBy(12.dp)){
    group.forEach{mode->
     Column(Modifier.weight(1f).padding(bottom=12.dp).background(Panel,RoundedCornerShape(18.dp)).clickable{onStart(mode)}.padding(16.dp)){
      Text(mode.mark,color=Gold,fontSize=28.sp);Spacer(Modifier.height(8.dp))
      Text(mode.title,color=Color.White,fontWeight=FontWeight.Bold,fontSize=15.sp);Spacer(Modifier.height(5.dp))
      Text(mode.subtitle,color=Muted,fontSize=11.sp,lineHeight=16.sp);Spacer(Modifier.height(8.dp))
      Text(bank.count{it.mode==mode}.toString()+" تحديات",color=Mint,fontSize=11.sp)
     }
    }
   }
  }
  Text("تقدمك محفوظ على هذا الجهاز",color=Mint,fontSize=12.sp,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center)
 }
}
@Composable private fun Stat(label:String,value:String){Column{Text(value,color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold);Text(label,color=Muted,fontSize=10.sp)}}
@Composable private fun ChallengeScreen(c:Challenge,number:Int,total:Int,onBack:()->Unit,onNext:(Boolean)->Unit){
 var clueCount by remember(c){mutableIntStateOf(1)}
 var answer by remember(c){mutableStateOf("")}
 var checked by remember(c){mutableStateOf(false)}
 val correct=c.answers.any{normalize(answer)==normalize(it)}
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)){
  TextButton(onClick=onBack){Text("→ العودة للرئيسية",color=Gold)}
  Spacer(Modifier.height(8.dp))
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(c.mode.title,color=Gold,fontSize=15.sp,fontWeight=FontWeight.Bold);Text("$number / $total",color=Muted,fontSize=12.sp)}
  Text(c.category,color=Muted,fontSize=12.sp);Spacer(Modifier.height(14.dp))
  LinearProgressIndicator(progress={number.toFloat()/total.coerceAtLeast(1)},modifier=Modifier.fillMaxWidth(),color=Mint,trackColor=Panel)
  Spacer(Modifier.height(25.dp));Text(c.prompt,color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Bold,lineHeight=35.sp)
  Spacer(Modifier.height(22.dp));Text("تلميحات تساعدك",color=Gold,fontWeight=FontWeight.Bold)
  c.clues.take(clueCount).forEach{Text("•  $it",color=Color.White,modifier=Modifier.fillMaxWidth().padding(top=10.dp).background(Panel,RoundedCornerShape(12.dp)).padding(14.dp))}
  if(clueCount<c.clues.size&&!checked)TextButton(onClick={clueCount++}){Text("اكشف تلميحاً إضافياً",color=Mint)}
  Spacer(Modifier.height(15.dp));Text("إجابتك",color=Gold,fontWeight=FontWeight.Bold)
  BasicTextField(value=answer,onValueChange={answer=it},enabled=!checked,singleLine=true,modifier=Modifier.fillMaxWidth().background(Color(0xFF203244),RoundedCornerShape(14.dp)).padding(16.dp),textStyle=TextStyle(color=Color.White,fontSize=16.sp),decorationBox={inner->if(answer.isEmpty())Text("اكتب إجابتك هنا…",color=Muted);inner()})
  Spacer(Modifier.height(15.dp))
  Button(onClick={checked=true},enabled=answer.isNotBlank()&&!checked,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Gold,contentColor=Ink)){Text("تحقق من الإجابة",fontWeight=FontWeight.Bold)}
  if(checked){
   Spacer(Modifier.height(18.dp));Text(if(correct)"إجابة صحيحة! أحسنت 🎉" else "ليست الإجابة الصحيحة هذه المرة",color=if(correct)Mint else Gold,fontWeight=FontWeight.Bold,fontSize=18.sp)
   if(!correct)Text("الإجابة النموذجية: "+c.answers.first(),color=Gold,modifier=Modifier.padding(top=8.dp))
   Text(c.explanation,color=Color.White,modifier=Modifier.padding(top=9.dp),lineHeight=24.sp);Spacer(Modifier.height(14.dp))
   Button(onClick={onNext(correct)},modifier=Modifier.fillMaxWidth()){Text(if(number==total)"ابدأ النمط من جديد" else "التحدي التالي")}
  }
 }
}
