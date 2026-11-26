package com.knowledgechallenge.app

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection

private val Ink = Color(0xFF091522)
private val Panel = Color(0xFF142536)
private val Gold = Color(0xFFE7B95D)
private val Mint = Color(0xFF55D6BE)
private val Muted = Color(0xFF9FB0BF)

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContent { KnowledgeApp() }
 }
}
private enum class Mode(val title: String, val subtitle: String, val mark: String) {
 DETECTIVE("المحقق الذكي","اجمع الأدلة واكشف الحقيقة","⌕"),
 RIDDLE("اكشف اللغز","فكّر بطريقة مختلفة","◇"),
 CORRECT("صحّح المعلومة","اكتشف الخطأ وصححه","✓"),
 MEMORY("تحدي الذاكرة","اختبر قوة ملاحظتك","▦"),
 PATTERN("أكمل الشفرة","اكتشف النمط الخفي","⌘"),
 WRITE("سؤال بلا خيارات","اكتب الإجابة من تفكيرك","✎")
}
private data class Challenge(val mode: Mode,val category:String,val prompt:String,val clues:List<String>,val answer:String,val explanation:String)
private val challenges = listOf(
 Challenge(Mode.DETECTIVE,"جغرافيا","أنا دولة عربية يمر بها خط الاستواء؟",listOf("تقع في قارة أفريقيا","لها ساحل على المحيط الهندي","عاصمتها موروني"),"جزر القمر","جزر القمر دولة جزرية تقع في المحيط الهندي قبالة الساحل الشرقي لأفريقيا."),
 Challenge(Mode.RIDDLE,"ألغاز منطقية","شيء كلما أخذت منه كبر، فما هو؟",listOf("فكر في شيء يتسع بالنقصان"),"الحفرة","كلما أزلت من التراب اتسعت الحفرة."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: الشمس تدور حول الأرض.",listOf("حدد الجزء العلمي غير الصحيح ثم اكتب التصحيح"),"الأرض تدور حول الشمس","الأرض والكواكب تدور حول الشمس، بينما تدور الأرض حول محورها أيضاً."),
 Challenge(Mode.PATTERN,"رياضيات","ما العدد التالي في السلسلة؟ 2، 6، 12، 20، 30، ؟",listOf("الفروق بين الأعداد هي 4، 6، 8، 10"),"42","الفروق تزداد بمقدار 2؛ الفرق التالي 12، إذن 30 + 12 = 42."),
 Challenge(Mode.WRITE,"ثقافة عامة","ما اسم العملية التي تصنع بها النباتات غذاءها باستخدام ضوء الشمس؟",listOf("تحدث في الأوراق وتحتاج إلى الماء وثاني أكسيد الكربون"),"البناء الضوئي","البناء الضوئي يحول الطاقة الضوئية إلى طاقة كيميائية مخزنة في الغذاء."),
 Challenge(Mode.MEMORY,"ذاكرة وملاحظة","تذكّر العناصر: قمر، مفتاح، نخلة، كتاب. ما العنصر الثاني؟",listOf("خذ لحظة لتصور العناصر بالترتيب"),"مفتاح","العنصر الثاني في التسلسل هو المفتاح.")
)
@Composable private fun KnowledgeApp() {
 var selected by remember { mutableStateOf<Mode?>(null) }
 var score by remember { mutableIntStateOf(0) }
 var solved by remember { mutableIntStateOf(0) }
 var index by remember { mutableIntStateOf(0) }
 CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
  MaterialTheme(colorScheme=darkColorScheme(background=Ink,surface=Panel,primary=Gold,secondary=Mint,onBackground=Color.White,onSurface=Color.White)) {
   Surface(Modifier.fillMaxSize(),color=Ink) {
    if(selected==null) HomeScreen(score,solved) { mode -> selected=mode; index=challenges.indexOfFirst { it.mode==mode }.let { if(it<0) 0 else it } }
    else ChallengeScreen(challenges[index%challenges.size],{selected=null}) { ok -> if(ok){score+=10;solved++};index=(index+1)%challenges.size;selected=null }
   }
  }
 }
}
@Composable private fun HomeScreen(score:Int,solved:Int,onStart:(Mode)->Unit) {
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
   Column { Text("تحدي المعرفة",color=Gold,fontSize=27.sp,fontWeight=FontWeight.ExtraBold);Text("فكّر أبعد من الإجابة",color=Muted,fontSize=13.sp) }
   Text("✦",color=Gold,fontSize=34.sp)
  }
  Spacer(Modifier.height(22.dp))
  Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF203D4B),Color(0xFF172536))),RoundedCornerShape(24.dp)).padding(20.dp)) {
   Text("مساحة العقول الفضولية",color=Gold,fontSize=13.sp)
   Spacer(Modifier.height(7.dp))
   Text("المعرفة ليست حفظاً…\nإنها طريقة تفكير.",color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Bold,lineHeight=32.sp)
   Spacer(Modifier.height(18.dp))
   Row(horizontalArrangement=Arrangement.spacedBy(24.dp)){Stat("النقاط",score.toString());Stat("تحديات محلولة",solved.toString());Stat("رتبتك",if(score>=100)"باحث" else "مستكشف")}
  }
  Spacer(Modifier.height(25.dp))
  Text("اختر طريقة التحدي",color=Color.White,fontSize=19.sp,fontWeight=FontWeight.Bold)
  Text("كل نمط يختبر مهارة مختلفة في عقلك",color=Muted,fontSize=12.sp)
  Spacer(Modifier.height(14.dp))
  Mode.values().toList().chunked(2).forEach { group ->
   Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
    group.forEach { mode ->
     Column(Modifier.weight(1f).padding(bottom=12.dp).background(Panel,RoundedCornerShape(18.dp)).clickable{onStart(mode)}.padding(15.dp)) {
      Text(mode.mark,color=Gold,fontSize=27.sp);Spacer(Modifier.height(8.dp))
      Text(mode.title,color=Color.White,fontWeight=FontWeight.Bold,fontSize=15.sp)
      Spacer(Modifier.height(4.dp));Text(mode.subtitle,color=Muted,fontSize=11.sp,lineHeight=16.sp)
     }
    }
    if(group.size==1) Spacer(Modifier.weight(1f))
   }
  }
  Text("التحديات تتجدد — تعلّم شيئاً جديداً كل يوم",color=Mint,fontSize=12.sp,modifier=Modifier.fillMaxWidth(),textAlign=TextAlign.Center)
 }
}
@Composable private fun Stat(label:String,value:String) {
 Column { Text(value,color=Color.White,fontSize=19.sp,fontWeight=FontWeight.Bold);Text(label,color=Muted,fontSize=10.sp) }
}
@Composable private fun ChallengeScreen(challenge:Challenge,onBack:()->Unit,onNext:(Boolean)->Unit) {
 var clueCount by remember(challenge){mutableIntStateOf(1)}
 var answer by remember(challenge){mutableStateOf("")}
 var revealed by remember(challenge){mutableStateOf(false)}
 var completed by remember(challenge){mutableStateOf(false)}
 val correct=answer.trim().equals(challenge.answer,ignoreCase=true)
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
  TextButton(onClick=onBack){Text("→ العودة للرئيسية",color=Gold)}
  Spacer(Modifier.height(8.dp))
  Text(challenge.mode.title,color=Gold,fontSize=14.sp,fontWeight=FontWeight.Bold)
  Text(challenge.category,color=Muted,fontSize=12.sp)
  Spacer(Modifier.height(18.dp))
  LinearProgressIndicator(progress={clueCount.toFloat()/challenge.clues.size.coerceAtLeast(1)},modifier=Modifier.fillMaxWidth(),color=Mint,trackColor=Panel)
  Spacer(Modifier.height(24.dp))
  Text(challenge.prompt,color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Bold,lineHeight=34.sp)
  Spacer(Modifier.height(20.dp))
  Text("الأدلة والتلميحات",color=Gold,fontWeight=FontWeight.Bold)
  challenge.clues.take(clueCount).forEach { clue -> Text("•  $clue",color=Color.White,modifier=Modifier.fillMaxWidth().padding(top=10.dp).background(Panel,RoundedCornerShape(12.dp)).padding(13.dp)) }
  if(clueCount<challenge.clues.size&&!revealed) TextButton(onClick={clueCount++}){Text("اكشف تلميحاً إضافياً",color=Mint)}
  Spacer(Modifier.height(12.dp))
  Text("اكتب إجابتك",color=Gold,fontWeight=FontWeight.Bold)
  BasicTextField(value=answer,onValueChange={answer=it},enabled=!completed,singleLine=true,modifier=Modifier.fillMaxWidth().background(Color(0xFF203244),RoundedCornerShape(14.dp)).padding(16.dp),textStyle=androidx.compose.ui.text.TextStyle(color=Color.White,fontSize=16.sp),decorationBox={inner->if(answer.isEmpty())Text("فكّر ثم اكتب هنا…",color=Muted);inner()})
  Spacer(Modifier.height(14.dp))
  Button(onClick={completed=true;revealed=true},enabled=answer.isNotBlank()&&!completed,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Gold,contentColor=Ink)){Text("تحقق من الإجابة",fontWeight=FontWeight.Bold)}
  if(revealed) {
   Spacer(Modifier.height(16.dp))
   Text(if(correct)"أحسنت! إجابة صحيحة 🎉" else "الإجابة النموذجية: " + challenge.answer,color=if(correct)Mint else Gold,fontWeight=FontWeight.Bold,fontSize=17.sp)
   Text(challenge.explanation,color=Color.White,modifier=Modifier.padding(top=8.dp),lineHeight=23.sp)
   Spacer(Modifier.height(12.dp))
   Button(onClick={onNext(correct)},modifier=Modifier.fillMaxWidth()){Text("التحدي التالي")}
  }
 }
}