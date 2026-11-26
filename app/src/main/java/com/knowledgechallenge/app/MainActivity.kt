package com.knowledgechallenge.app
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
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

private val Ink=Color(0xFF29243F)
private val Panel=Color(0xFFFFFFFF)
private val Gold=Color(0xFFFF8A45)
private val Mint=Color(0xFF27BFA8)
private val Muted=Color(0xFF858097)
private val Canvas=Color(0xFFF5F2FC)
private val Purple=Color(0xFF7657D6)

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
  MaterialTheme(colorScheme=lightColorScheme(background=Canvas,surface=Panel,primary=Gold,secondary=Mint,onBackground=Ink,onSurface=Ink)){
   Surface(Modifier.fillMaxSize(),color=Canvas){
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
 val bg=Color(0xFFF5F2FC); val ink=Color(0xFF29243F); val muted=Color(0xFF858097)
 Column(Modifier.fillMaxSize().background(bg).verticalScroll(rememberScrollState()).padding(horizontal=18.dp, vertical=16.dp)){
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){
   Row(verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(48.dp).background(Brush.linearGradient(listOf(Color(0xFF7657D6),Color(0xFFB28CF6))),RoundedCornerShape(16.dp)),contentAlignment=Alignment.Center){Text("🧠",fontSize=26.sp)}
    Spacer(Modifier.width(10.dp));Column{Text("تحدّي المعرفة",color=ink,fontSize=21.sp,fontWeight=FontWeight.ExtraBold);Text("العب بذكاء… واصنع إنجازك",color=muted,fontSize=11.sp)}
   }
   Row(Modifier.background(Color.White,RoundedCornerShape(20.dp)).padding(horizontal=12.dp,vertical=9.dp),verticalAlignment=Alignment.CenterVertically){Text("🪙",fontSize=16.sp);Spacer(Modifier.width(5.dp));Text(score.toString(),color=ink,fontWeight=FontWeight.Bold,fontSize=14.sp)}
  }
  Spacer(Modifier.height(18.dp))
  Box(Modifier.fillMaxWidth().height(205.dp).background(Brush.linearGradient(listOf(Color(0xFF7356D8),Color(0xFFAA83F2),Color(0xFFFFB27E))),RoundedCornerShape(28.dp)).padding(18.dp)){
   Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(.65f)){
    Text("تحدّى عقلك اليوم!",color=Color.White,fontSize=23.sp,fontWeight=FontWeight.ExtraBold,lineHeight=30.sp)
    Spacer(Modifier.height(7.dp));Text("أسئلة ممتعة، ألغاز ذكية، ومنافسة ترفع رصيدك من المعرفة.",color=Color.White,fontSize=12.sp,lineHeight=19.sp)
    Spacer(Modifier.height(13.dp))
    Row(Modifier.background(Color.White,RoundedCornerShape(22.dp)).clickable{onStart(Mode.RIDDLE)}.padding(horizontal=16.dp,vertical=10.dp),verticalAlignment=Alignment.CenterVertically){Text("ابدأ التحدي",color=Color(0xFF7356D8),fontWeight=FontWeight.Bold,fontSize=12.sp);Spacer(Modifier.width(7.dp));Text("←",color=Color(0xFF7356D8),fontWeight=FontWeight.Bold)}
   }
   Text("🏆",fontSize=76.sp,modifier=Modifier.align(Alignment.CenterEnd).padding(end=1.dp))
   Text("✨",fontSize=28.sp,modifier=Modifier.align(Alignment.TopEnd).padding(end=12.dp))
   Text("🧩",fontSize=30.sp,modifier=Modifier.align(Alignment.BottomEnd).padding(end=16.dp,bottom=8.dp))
  }
  Spacer(Modifier.height(17.dp))
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(9.dp)){
   MiniStat("⭐","النقاط",score.toString(),Modifier.weight(1f))
   MiniStat("🎯","تم حلها",solved.toString(),Modifier.weight(1f))
   MiniStat("🔥","متتالية",streak.toString(),Modifier.weight(1f))
  }
  Spacer(Modifier.height(22.dp))
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("اختر مغامرتك",color=ink,fontSize=19.sp,fontWeight=FontWeight.ExtraBold);Text("٦ أنماط",color=Color(0xFF7657D6),fontSize=11.sp,fontWeight=FontWeight.Bold)}
  Text("كل نمط يختبر مهارة مختلفة لديك",color=muted,fontSize=12.sp,modifier=Modifier.padding(top=4.dp,bottom=13.dp))
  val colors=listOf(Color(0xFFFFEEE4),Color(0xFFEDE8FF),Color(0xFFE1F7F4),Color(0xFFFFF3D9),Color(0xFFE5F0FF),Color(0xFFFFE8F1))
  val emojis=listOf("🕵️","🧩","⚡","🧠","🔢","✍️")
  Mode.values().toList().chunked(2).forEachIndexed{row,group->
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(11.dp)){
    group.forEachIndexed{col,mode->
     val i=row*2+col
     Column(Modifier.weight(1f).padding(bottom=11.dp).background(Color.White,RoundedCornerShape(21.dp)).clickable{onStart(mode)}.padding(14.dp)){
      Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
       Box(Modifier.size(46.dp).background(colors[i],RoundedCornerShape(15.dp)),contentAlignment=Alignment.Center){Text(emojis[i],fontSize=25.sp)}
       Text("↗",color=muted,fontSize=17.sp)
      }
      Spacer(Modifier.height(11.dp));Text(mode.title,color=ink,fontWeight=FontWeight.ExtraBold,fontSize=14.sp)
      Text(mode.subtitle,color=muted,fontSize=10.sp,lineHeight=15.sp,modifier=Modifier.padding(top=4.dp),maxLines=2)
      Spacer(Modifier.height(9.dp));Text(bank.count{it.mode==mode}.toString()+" جولات متاحة",color=if(i%2==0)Color(0xFFFF8A45) else Color(0xFF7657D6),fontSize=10.sp,fontWeight=FontWeight.Bold)
     }
    }
   }
  }
  Row(Modifier.fillMaxWidth().background(Color(0xFFEDE8FF),RoundedCornerShape(16.dp)).padding(13.dp),verticalAlignment=Alignment.CenterVertically){Text("💡",fontSize=22.sp);Spacer(Modifier.width(9.dp));Column{Text("معلومة صغيرة تصنع فرقاً",color=Color(0xFF7657D6),fontSize=12.sp,fontWeight=FontWeight.Bold);Text("استمر في اللعب، وتعلّم شيئاً جديداً كل يوم.",color=muted,fontSize=10.sp)}}
  Spacer(Modifier.height(18.dp))
 }
}
@Composable private fun MiniStat(icon:String,label:String,value:String,modifier:Modifier=Modifier){
 Column(modifier.background(Color.White,RoundedCornerShape(18.dp)).padding(vertical=12.dp,horizontal=10.dp)){
  Row(verticalAlignment=Alignment.CenterVertically){Text(icon,fontSize=16.sp);Spacer(Modifier.width(6.dp));Text(value,color=Color(0xFF29243F),fontSize=18.sp,fontWeight=FontWeight.ExtraBold)}
  Text(label,color=Color(0xFF858097),fontSize=10.sp,modifier=Modifier.padding(top=5.dp))
 }
}
@Composable private fun Stat(label:String,value:String){Column{Text(value,color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold);Text(label,color=Muted,fontSize=10.sp)}}
@Composable private fun ChallengeScreen(c:Challenge,number:Int,total:Int,onBack:()->Unit,onNext:(Boolean)->Unit){
 var clueCount by remember(c){mutableIntStateOf(1)}
 var answer by remember(c){mutableStateOf("")}
 var checked by remember(c){mutableStateOf(false)}
 var revealMemory by remember(c){mutableStateOf(true)}
 val correct=c.answers.any{normalize(answer)==normalize(it)}
 LaunchedEffect(c){if(c.mode==Mode.MEMORY){revealMemory=true;kotlinx.coroutines.delay(3500);revealMemory=false}}
 Column(Modifier.fillMaxSize().background(Canvas).verticalScroll(rememberScrollState()).padding(18.dp)){
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){TextButton(onClick=onBack){Text("→ رجوع",color=Purple,fontWeight=FontWeight.Bold)};Spacer(Modifier.weight(1f));Text("تحدّي المعرفة",color=Ink,fontWeight=FontWeight.ExtraBold)}
  Spacer(Modifier.height(12.dp))
  Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Purple,Color(0xFFAA83F2))),RoundedCornerShape(24.dp)).padding(18.dp)){
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(c.mode.title,color=Color.White,fontSize=15.sp,fontWeight=FontWeight.Bold);Text("السؤال "+number+" من "+total,color=Color.White,fontSize=12.sp)}
   Spacer(Modifier.height(13.dp));LinearProgressIndicator(progress={number.toFloat()/total.coerceAtLeast(1)},modifier=Modifier.fillMaxWidth().height(7.dp),color=Color(0xFFFFD18B),trackColor=Color(0x557FFFFFFF))
   Spacer(Modifier.height(12.dp));Text("🎯  "+c.category,color=Color.White,fontSize=12.sp)
  }
  Spacer(Modifier.height(18.dp))
  Column(Modifier.fillMaxWidth().background(Color.White,RoundedCornerShape(23.dp)).padding(19.dp)){
   Text(if(c.mode==Mode.MEMORY&&revealMemory)"احفظ جيداً… ستختفي العناصر بعد لحظات" else "السؤال",color=Gold,fontSize=13.sp,fontWeight=FontWeight.ExtraBold)
   Spacer(Modifier.height(10.dp));Text(if(c.mode==Mode.MEMORY&&!revealMemory)"انتهى وقت الحفظ! أجب من ذاكرتك." else c.prompt,color=Ink,fontSize=21.sp,fontWeight=FontWeight.Bold,lineHeight=32.sp)
   if(c.mode==Mode.MEMORY&&revealMemory){Spacer(Modifier.height(10.dp));Text("⏳ احفظ التسلسل الآن",color=Purple,fontSize=12.sp,fontWeight=FontWeight.Bold)}
  }
  Spacer(Modifier.height(16.dp))
  Row(verticalAlignment=Alignment.CenterVertically){Text("💡",fontSize=20.sp);Spacer(Modifier.width(7.dp));Text("تلميحات ذكية",color=Ink,fontWeight=FontWeight.Bold,fontSize=15.sp)}
  c.clues.take(clueCount).forEach{Text("•  "+it,color=Ink,modifier=Modifier.fillMaxWidth().padding(top=9.dp).background(Color.White,RoundedCornerShape(14.dp)).padding(14.dp),fontSize=13.sp)}
  if(clueCount<c.clues.size&&!checked)TextButton(onClick={clueCount++}){Text("اكشف تلميحاً إضافياً",color=Purple)}
  Spacer(Modifier.height(12.dp));Text("اكتب إجابتك",color=Ink,fontWeight=FontWeight.Bold,fontSize=14.sp)
  BasicTextField(value=answer,onValueChange={answer=it},enabled=!checked,singleLine=true,modifier=Modifier.fillMaxWidth().padding(top=9.dp).background(Color.White,RoundedCornerShape(15.dp)).padding(17.dp),textStyle=TextStyle(color=Ink,fontSize=16.sp),decorationBox={inner->if(answer.isEmpty())Text("اكتب إجابتك هنا…",color=Muted);inner()})
  Spacer(Modifier.height(15.dp))
  Button(onClick={checked=true},enabled=answer.isNotBlank()&&!checked,modifier=Modifier.fillMaxWidth().height(53.dp),shape=RoundedCornerShape(16.dp),colors=ButtonDefaults.buttonColors(containerColor=Gold,contentColor=Color.White)){Text("تحقّق من الإجابة",fontWeight=FontWeight.Bold)}
  if(checked){
   Spacer(Modifier.height(16.dp));Column(Modifier.fillMaxWidth().background(if(correct)Color(0xFFE4F8F1) else Color(0xFFFFF0E8),RoundedCornerShape(18.dp)).padding(16.dp)){
    Text(if(correct)"🎉 إجابة صحيحة! أحسنت" else "💪 حاول مرة أخرى في الجولة القادمة",color=if(correct)Color(0xFF159A7C) else Color(0xFFCF6C37),fontWeight=FontWeight.ExtraBold,fontSize=17.sp)
    if(!correct)Text("الإجابة النموذجية: "+c.answers.first(),color=Ink,modifier=Modifier.padding(top=8.dp),fontSize=13.sp)
    Text(c.explanation,color=Ink,modifier=Modifier.padding(top=9.dp),lineHeight=23.sp,fontSize=13.sp)
   }
   Spacer(Modifier.height(14.dp));Button(onClick={onNext(correct)},modifier=Modifier.fillMaxWidth().height(52.dp),shape=RoundedCornerShape(16.dp),colors=ButtonDefaults.buttonColors(containerColor=Purple)){Text(if(number==total)"ابدأ النمط من جديد" else "التحدي التالي",fontWeight=FontWeight.Bold)}
  }
 }
}
