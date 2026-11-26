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
 Challenge(Mode.WRITE,"ثقافة عامة","كم عدد أضلاع الشكل السداسي؟",listOf("اسمه يدل على العدد"),listOf("6","٦","ستة"),"الشكل السداسي له ستة أضلاع."),

 Challenge(Mode.DETECTIVE,"علوم الفضاء","ما اسم المجرة التي تنتمي إليها المجموعة الشمسية؟",listOf("تُعرف باسم درب التبانة","تبدو كشريط مضيء في السماء"),listOf("درب التبانة","مجرة درب التبانة"),"تقع المجموعة الشمسية في مجرة درب التبانة."),
 Challenge(Mode.DETECTIVE,"تاريخ","ما اسم المدينة اليمنية القديمة المشهورة بسدها التاريخي؟",listOf("ارتبط اسمها بمملكة سبأ","ورد ذكرها في قصة بلقيس"),listOf("مأرب","مارب"),"مأرب من أهم مراكز مملكة سبأ، واشتهرت بسدها التاريخي."),
 Challenge(Mode.DETECTIVE,"جغرافيا","ما عاصمة اليابان؟",listOf("مدينة كبرى تقع في جزيرة هونشو","تُعد مركزاً سياسياً واقتصادياً"),listOf("طوكيو"),"طوكيو هي عاصمة اليابان."),
 Challenge(Mode.DETECTIVE,"علوم","ما العنصر الكيميائي الذي رمزه O؟",listOf("ضروري لتنفس معظم الكائنات الحية","يشكل جزءاً من الهواء والماء"),listOf("الأكسجين","اكسجين"),"رمز الأكسجين الكيميائي هو O."),
 Challenge(Mode.DETECTIVE,"جغرافيا","ما أطول نهر في أفريقيا؟",listOf("يمر بعدة دول أفريقية","ارتبط به تاريخ مصر القديم"),listOf("نهر النيل","النيل"),"نهر النيل هو أطول أنهار أفريقيا."),
 Challenge(Mode.DETECTIVE,"تاريخ","من القائد المسلم الذي ارتبط اسمه بفتح الأندلس؟",listOf("عبر المضيق إلى شبه الجزيرة الإيبيرية","سُمي مضيق جبل طارق باسمه"),listOf("طارق بن زياد"),"قاد طارق بن زياد الجيش الذي بدأ فتح الأندلس."),
 Challenge(Mode.DETECTIVE,"علوم","ما العضو الذي يضخ الدم إلى أنحاء الجسم؟",listOf("يعمل باستمرار","يتكون من أربع حجرات عند الإنسان"),listOf("القلب"),"القلب يضخ الدم عبر الأوعية الدموية."),
 Challenge(Mode.DETECTIVE,"جغرافيا","ما عاصمة المملكة العربية السعودية؟",listOf("تقع في وسط البلاد تقريباً","من أكبر مدن شبه الجزيرة العربية"),listOf("الرياض"),"الرياض هي عاصمة المملكة العربية السعودية."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي له عين واحدة ولا يرى؟",listOf("قد تستخدمه في الخياطة","يمر الخيط من فتحته"),listOf("الإبرة"),"عين الإبرة هي الفتحة التي يمر منها الخيط."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي كلما زاد نقص؟",listOf("يرتبط بمرور الأيام","يستهلكه الإنسان في حياته"),listOf("العمر"),"كلما مر الوقت زاد العمر ونقص المتبقي منه."),
 Challenge(Mode.RIDDLE,"تفكير منطقي","أم أحمد لديها أربعة أبناء: شمال وجنوب وشرق، ما اسم الرابع؟",listOf("اقرأ بداية اللغز جيداً","الاسم مذكور في أول جملة"),listOf("أحمد"),"اللغز يقول أم أحمد، إذن اسم الابن الرابع أحمد."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي يسمع بلا أذن ويتكلم بلا لسان؟",listOf("قد يعود إليك صوتك منه","يظهر في الجبال والأماكن الواسعة"),listOf("الصدى"),"الصدى هو انعكاس الصوت."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي يملأ الغرفة ولا يشغل حيزاً؟",listOf("يساعدك على الرؤية","يأتي من الشمس أو المصباح"),listOf("الضوء"),"الضوء ينتشر في المكان."),
 Challenge(Mode.RIDDLE,"تفكير منطقي","لديك 3 تفاحات وأخذتَ اثنتين، كم تفاحة أصبحت معك؟",listOf("انتبه إلى كلمة أخذت","السؤال عما أصبح معك أنت"),listOf("تفاحتان","2","٢"),"أخذت تفاحتين، إذن أصبح معك تفاحتان."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي له رقبة وليس له رأس؟",listOf("قد يحتوي الماء أو العصير","يوجد في المطبخ"),listOf("الزجاجة","القارورة"),"للزجاجة رقبة ولا رأس لها."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي يمشي بلا قدمين ويبكي بلا عينين؟",listOf("قد تراه في السماء","يرتبط بالمطر"),listOf("السحاب","الغيم"),"السحاب يتحرك في السماء وينزل المطر."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: الماء يتكون من ذرتي أكسجين وذرة هيدروجين.",listOf("تذكر الصيغة H2O"),listOf("الماء يتكون من ذرتي هيدروجين وذرة أكسجين","ذرتي هيدروجين وذرة أكسجين"),"جزيء الماء H2O يتكون من ذرتي هيدروجين وذرة أكسجين."),
 Challenge(Mode.CORRECT,"جغرافيا","صحّح العبارة: قارة أفريقيا تقع بالكامل في نصف الكرة الشمالي.",listOf("يمر خط الاستواء عبر القارة"),listOf("تقع أفريقيا في نصفي الكرة الشمالي والجنوبي","يمر خط الاستواء بأفريقيا"),"يمر خط الاستواء عبر أفريقيا."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: الصوت ينتقل في الفراغ.",listOf("الصوت يحتاج وسطاً مادياً"),listOf("الصوت لا ينتقل في الفراغ","يحتاج الصوت إلى وسط مادي"),"الصوت موجة ميكانيكية تحتاج إلى وسط مادي."),
 Challenge(Mode.CORRECT,"رياضيات","صحّح العبارة: حاصل ضرب أي عدد في الصفر يساوي العدد نفسه.",listOf("تذكر قاعدة الضرب في الصفر"),listOf("حاصل ضرب أي عدد في الصفر يساوي صفراً","الناتج صفر"),"أي عدد مضروب في الصفر يساوي صفراً."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: يتجمد الماء عند 100 درجة مئوية.",listOf("100 درجة هي درجة الغليان تقريباً","فكر في الثلج"),listOf("يتجمد الماء عند صفر درجة مئوية","صفر مئوية"),"يتجمد الماء النقي عند نحو صفر درجة مئوية."),
 Challenge(Mode.CORRECT,"جغرافيا","صحّح العبارة: البحر الأحمر يقع بين قارتي أوروبا وأمريكا.",listOf("يقع بين شمال شرق أفريقيا وشبه الجزيرة العربية"),listOf("يقع بين أفريقيا وآسيا","بين أفريقيا وشبه الجزيرة العربية"),"يفصل البحر الأحمر أجزاء من أفريقيا عن شبه الجزيرة العربية."),
 Challenge(Mode.CORRECT,"لغة عربية","صحّح العبارة: الفاعل يكون منصوباً دائماً.",listOf("اسأل: من قام بالفعل؟","تذكر علامة الرفع الأصلية"),listOf("الفاعل مرفوع","الفاعل يكون مرفوعاً"),"الفاعل مرفوع."),
 Challenge(Mode.CORRECT,"علوم","صحّح العبارة: النباتات تحصل على غذائها من التربة فقط.",listOf("تحتاج إلى الماء والضوء وثاني أكسيد الكربون"),listOf("تصنع النباتات غذاءها بالبناء الضوئي","النبات يصنع غذاءه"),"تصنع النباتات غذاءها بالبناء الضوئي."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ الترتيب: تفاحة، قلم، قمر، مفتاح، كتاب. ما العنصر الثاني؟",listOf("تخيل الأشياء بالترتيب"),listOf("قلم","القلم"),"العنصر الثاني هو القلم."),
 Challenge(Mode.MEMORY,"ذاكرة","تذكّر: 4، 9، 2، 7، 5. ما الرقم الخامس؟",listOf("تتبع التسلسل من بدايته"),listOf("5","٥","خمسة"),"الرقم الخامس هو 5."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: أحمر، بنفسجي، أبيض، أخضر، أسود. ما اللون الرابع؟",listOf("رتب الألوان في ذهنك"),listOf("أخضر","الاخضر","الأخضر"),"اللون الرابع هو الأخضر."),
 Challenge(Mode.MEMORY,"ملاحظة","تذكّر: صنعاء، عدن، تعز، إب. ما المدينة الثالثة؟",listOf("تذكر ترتيب المدن"),listOf("تعز"),"المدينة الثالثة هي تعز."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: 12، 5، 18، 3، 21. ما العدد الأول؟",listOf("ابدأ من أول التسلسل"),listOf("12","١٢","اثنا عشر"),"العدد الأول هو 12."),
 Challenge(Mode.MEMORY,"ذاكرة","تذكّر: نسر، حصان، سمكة، أرنب. ما الحيوان الثاني؟",listOf("تخيل الحيوانات بالترتيب"),listOf("حصان","الحصان"),"الحيوان الثاني هو الحصان."),
 Challenge(Mode.MEMORY,"ملاحظة","احفظ: كتاب، نافذة، ساعة، كرسي، مصباح. ما العنصر الأخير؟",listOf("تذكر نهاية القائمة"),listOf("مصباح","المصباح"),"العنصر الأخير هو المصباح."),
 Challenge(Mode.MEMORY,"ذاكرة","تذكّر: 6، 1، 8، 4، 2، 9. ما الرقم الثالث؟",listOf("عدّ من اليسار إلى اليمين"),listOf("8","٨","ثمانية"),"الرقم الثالث هو 8."),
 Challenge(Mode.PATTERN,"رياضيات","ما العدد التالي؟ 3، 6، 9، 12، ؟",listOf("أضف المقدار نفسه كل مرة"),listOf("15","خمسة عشر"),"النمط يزيد بمقدار 3."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل: 100، 90، 80، 70، ؟",listOf("ينقص العدد بمقدار ثابت"),listOf("60","ستون"),"ينقص كل حد بمقدار 10."),
 Challenge(Mode.PATTERN,"منطق","ما العدد التالي؟ 1، 1، 2، 3، 5، 8، ؟",listOf("كل حد يساوي مجموع الحدين السابقين"),listOf("13","ثلاثة عشر"),"العدد التالي 5 + 8 = 13."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل النمط: 2، 4، 8، 16، ؟",listOf("يتضاعف العدد كل مرة"),listOf("32","اثنان وثلاثون"),"كل عدد يساوي ضعف السابق."),
 Challenge(Mode.PATTERN,"رياضيات","ما العدد الناقص؟ 7، 14، 21، ؟، 35",listOf("الأعداد مضاعفات العدد 7"),listOf("28","ثمانية وعشرون"),"العدد الناقص 28."),
 Challenge(Mode.PATTERN,"منطق","ما العدد التالي؟ 1، 8، 27، 64، ؟",listOf("هذه مكعبات الأعداد الطبيعية"),listOf("125","مئة وخمسة وعشرون"),"العدد التالي 5³ = 125."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل: 50، 45، 40، 35، ؟",listOf("اطرح خمسة في كل خطوة"),listOf("30","ثلاثون"),"ينقص النمط بمقدار 5."),
 Challenge(Mode.PATTERN,"منطق","ما العدد التالي؟ 2، 3، 5، 8، 12، 17، ؟",listOf("الفروق تزداد واحداً كل مرة"),listOf("23","ثلاثة وعشرون"),"الفرق التالي 6 والنتيجة 23."),
 Challenge(Mode.WRITE,"علوم","ما الغاز الذي تمتصه النباتات من الهواء في البناء الضوئي؟",listOf("رمزه الكيميائي CO2"),listOf("ثاني أكسيد الكربون","ثاني اكسيد الكربون"),"تمتص النباتات ثاني أكسيد الكربون في البناء الضوئي."),
 Challenge(Mode.WRITE,"جغرافيا","ما أكبر محيطات العالم مساحة؟",listOf("يقع بين آسيا وأستراليا والأمريكيتين"),listOf("المحيط الهادئ","الهادئ"),"المحيط الهادئ هو أكبر محيطات الأرض."),
 Challenge(Mode.WRITE,"تاريخ","في أي قارة تقع الأهرامات المصرية؟",listOf("القارة التي تقع فيها مصر"),listOf("أفريقيا","افريقيا"),"تقع الأهرامات المصرية في قارة أفريقيا."),
 Challenge(Mode.WRITE,"علوم","ما وحدة قياس شدة التيار الكهربائي؟",listOf("سميت نسبة إلى العالم أندريه أمبير"),listOf("الأمبير","امبير","A"),"الأمبير وحدة قياس شدة التيار الكهربائي."),
 Challenge(Mode.WRITE,"لغة عربية","ما جمع كلمة كتاب؟",listOf("جمع تكسير شائع"),listOf("كتب","كُتب"),"جمع كتاب هو كتب."),
 Challenge(Mode.WRITE,"رياضيات","كم يساوي ربع العدد 100؟",listOf("اقسم 100 على أربعة"),listOf("25","٢٥","خمسة وعشرون"),"ربع 100 يساوي 25."),
 Challenge(Mode.WRITE,"علوم","ما أقرب نجم إلى كوكب الأرض؟",listOf("يمنح الأرض الضوء والحرارة نهاراً"),listOf("الشمس"),"الشمس أقرب نجم إلى الأرض."),
 Challenge(Mode.WRITE,"جغرافيا","ما عاصمة جمهورية مصر العربية؟",listOf("تقع على نهر النيل"),listOf("القاهرة"),"القاهرة هي عاصمة جمهورية مصر العربية."),
 Challenge(Mode.WRITE,"ثقافة عامة","كم يوماً في السنة العادية؟",listOf("السنة التي ليست كبيسة"),listOf("365","٣٦٥","ثلاثمئة وخمسة وستون"),"السنة العادية تتكون من 365 يوماً."),
 Challenge(Mode.WRITE,"علوم","ما اسم تحول الماء من الحالة السائلة إلى الغازية؟",listOf("يحدث عند سطح الماء أو بالغليان"),listOf("التبخر","تبخر"),"التبخر هو تحول السائل إلى غاز."),

 Challenge(Mode.DETECTIVE,"علوم","ما الغاز الذي يشكل النسبة الأكبر من الغلاف الجوي للأرض؟",listOf("يشكل قرابة 78% من الهواء"),listOf("النيتروجين","الآزوت"),"النيتروجين يشكل نحو 78% من الغلاف الجوي."),
 Challenge(Mode.DETECTIVE,"جغرافيا","ما عاصمة كندا؟",listOf("تقع في مقاطعة أونتاريو"),listOf("أوتاوا"),"أوتاوا هي العاصمة الاتحادية لكندا."),
 Challenge(Mode.DETECTIVE,"تاريخ","من الرحالة المغربي الشهير الذي جاب بلاداً كثيرة في القرن الرابع عشر؟",listOf("دوّن رحلاته في كتاب تحفة النظار"),listOf("ابن بطوطة"),"ابن بطوطة من أشهر الرحالة في التاريخ."),
 Challenge(Mode.DETECTIVE,"علوم","ما الكوكب الأكبر في المجموعة الشمسية؟",listOf("عملاق غازي وله بقعة حمراء عظيمة"),listOf("المشتري"),"المشتري أكبر كواكب المجموعة الشمسية."),
 Challenge(Mode.DETECTIVE,"جغرافيا","ما عاصمة تركيا؟",listOf("تقع في منطقة الأناضول"),listOf("أنقرة"),"أنقرة عاصمة تركيا."),
 Challenge(Mode.DETECTIVE,"علوم","ما الجزء المسؤول عن امتصاص معظم الماء في النبات؟",listOf("جزء يوجد غالباً تحت التربة"),listOf("الجذور","الجذر"),"تمتص الجذور الماء والأملاح من التربة."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي كلما مشى فقد جزءاً من ذيله؟",listOf("يستخدم لمعرفة الوقت قديماً","يذوب أثناء عمله"),listOf("الشمعة"),"الشمعة تقصر كلما احترقت."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي له أوراق وليس نباتاً وله جلد وليس حيواناً؟",listOf("تجد فيه الكلمات والقصص"),listOf("الكتاب"),"الكتاب له أوراق وغلاف."),
 Challenge(Mode.RIDDLE,"تفكير منطقي","رجل خرج تحت المطر بلا مظلة ولم يبتل شعره، لماذا؟",listOf("فكر في حالة شعره"),listOf("أصلع","لأنه أصلع"),"لم يكن لديه شعر ليبتل."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي تراه في الليل ثلاث مرات وفي النهار مرة واحدة؟",listOf("ابحث عن حرف في كلمتي الليل والنهار"),listOf("حرف اللام","اللام"),"حرف اللام يظهر مرتين في الليل ومرة في النهار؛ صياغة اللغز الشائعة تعتمد عد الحروف."),
 Challenge(Mode.RIDDLE,"تفكير","إذا تجاوزت المتسابق الذي في المركز الثاني، ففي أي مركز تصبح؟",listOf("أنت تأخذ مكانه مباشرة"),listOf("المركز الثاني","الثاني"),"عندما تتجاوز الثاني تصبح في المركز الثاني."),
 Challenge(Mode.RIDDLE,"ألغاز","ما الشيء الذي له مفاتيح كثيرة ولا يفتح باباً؟",listOf("تستخدمه للعزف"),listOf("البيانو"),"البيانو له مفاتيح موسيقية."),
 Challenge(Mode.CORRECT,"علوم","صحح: الأرض كوكب مسطح تماماً.",listOf("شكلها قريب من الكرة ومفلطح قليلاً عند القطبين"),listOf("الأرض شبه كروية","الأرض كروية تقريباً"),"الأرض شبه كروية ومفلطحة قليلاً عند القطبين."),
 Challenge(Mode.CORRECT,"علوم","صحح: الضوء أبطأ من الصوت في الهواء.",listOf("قارن سرعة البرق والرعد"),listOf("الضوء أسرع من الصوت"),"سرعة الضوء أكبر بكثير من سرعة الصوت."),
 Challenge(Mode.CORRECT,"جغرافيا","صحح: المحيط الأطلسي هو أكبر محيطات العالم.",listOf("هناك محيط أكبر منه بين آسيا والأمريكيتين"),listOf("المحيط الهادئ أكبر المحيطات"),"المحيط الهادئ أكبر من الأطلسي."),
 Challenge(Mode.CORRECT,"رياضيات","صحح: مجموع زوايا المثلث 180 درجة.",listOf("راجع القاعدة الهندسية الأساسية"),listOf("العبارة صحيحة","صحيح","180 درجة"),"العبارة صحيحة في الهندسة الإقليدية؛ مجموع زوايا المثلث 180 درجة."),
 Challenge(Mode.CORRECT,"علوم","صحح: القمر يصدر الضوء من نفسه مثل الشمس.",listOf("القمر يعكس ضوء الشمس"),listOf("القمر يعكس ضوء الشمس","لا يصدر ضوءاً من نفسه"),"القمر جسم معتم يعكس ضوء الشمس."),
 Challenge(Mode.CORRECT,"لغة عربية","صحح: إنَّ وأخواتها ترفع المبتدأ وتنصب الخبر.",listOf("تذكر عمل إنّ"),listOf("تنصب المبتدأ وترفع الخبر"),"إن وأخواتها تنصب المبتدأ ويسمى اسمها وترفع الخبر."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: قهوة، نافذة، طائر، مفتاح، سحابة. ما العنصر الثالث؟",listOf("رتب الكلمات في ذهنك"),listOf("طائر","الطائر"),"العنصر الثالث هو طائر."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: 15، 3، 22، 8، 11، 6. ما الرقم الرابع؟",listOf("عد من بداية السلسلة"),listOf("8","٨","ثمانية"),"الرقم الرابع هو 8."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: صنعاء، عدن، المكلا، تعز، إب. ما المدينة الخامسة؟",listOf("تذكر ترتيب المدن"),listOf("إب","اب"),"المدينة الخامسة هي إب."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: مثلث، دائرة، مربع، نجمة، مستطيل. ما الشكل الأول؟",listOf("تذكر بداية القائمة"),listOf("مثلث","المثلث"),"الشكل الأول مثلث."),
 Challenge(Mode.MEMORY,"ذاكرة","احفظ: فضة، ذهب، نحاس، حديد، ألمنيوم. ما المعدن الرابع؟",listOf("يستخدم في البناء وصناعة الأدوات"),listOf("الحديد"),"المعدن الرابع هو الحديد."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل: 4، 8، 16، 32، ؟",listOf("ضاعف العدد في كل مرة"),listOf("64","أربعة وستون"),"التالي 64."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل: 2، 5، 10، 17، 26، ؟",listOf("الفروق 3 ثم 5 ثم 7 ثم 9"),listOf("37","سبعة وثلاثون"),"الفرق التالي 11، إذن الناتج 37."),
 Challenge(Mode.PATTERN,"منطق","ما العدد التالي: 1، 4، 16، 64، ؟",listOf("اضرب في أربعة"),listOf("256","٢٥٦"),"كل حد يساوي السابق مضروباً في 4."),
 Challenge(Mode.PATTERN,"رياضيات","أكمل: 64، 32، 16، 8، ؟",listOf("اقسم على 2"),listOf("4","أربعة"),"كل حد نصف السابق."),
 Challenge(Mode.PATTERN,"رياضيات","ما العدد الناقص: 5، 10، ؟، 20، 25",listOf("أضف خمسة"),listOf("15","خمسة عشر"),"العدد الناقص 15."),
 Challenge(Mode.WRITE,"علوم","ما اسم الطبقة الغازية التي تحيط بالأرض؟",listOf("تحمينا وتحتوي الهواء"),listOf("الغلاف الجوي"),"الغلاف الجوي يحيط بالأرض."),
 Challenge(Mode.WRITE,"جغرافيا","ما عاصمة سلطنة عُمان؟",listOf("مدينة ساحلية على بحر العرب"),listOf("مسقط"),"مسقط عاصمة سلطنة عمان."),
 Challenge(Mode.WRITE,"علوم","ما اسم الجهاز الذي يقيس درجة الحرارة؟",listOf("يستخدم في الطقس والطب"),listOf("مقياس الحرارة","الترمومتر","ميزان الحرارة"),"يقاس مقدار الحرارة بمقياس الحرارة."),
 Challenge(Mode.WRITE,"رياضيات","كم يساوي 12 مضروباً في 12؟",listOf("12 × 10 ثم أضف 12 × 2"),listOf("144","١٤٤"),"12 × 12 = 144."),
 Challenge(Mode.WRITE,"تاريخ","ما اسم الكتابة القديمة التي استخدمها المصريون القدماء؟",listOf("نقوش ورموز على المعابد والبردي"),listOf("الهيروغليفية","الكتابة الهيروغليفية"),"الهيروغليفية نظام كتابة مصرية قديمة."),
 Challenge(Mode.WRITE,"علوم","ما العضو الذي ينقي الدم ويساعد على تكوين البول؟",listOf("يوجد منه اثنان عادة في جسم الإنسان"),listOf("الكلى","الكليتان"),"الكليتان ترشحان الدم وتساعدان على إخراج الفضلات.")


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
    else ChallengeScreen(current,qIndex+1,questions.size,score,{selected=null},{cost->score-=cost;prefs.edit().putInt("score",score).apply()}){ok->
     if(ok){score+=10;solved++;streak++;prefs.edit().putInt("score",score).putInt("solved",solved).putInt("streak",streak).apply()}
     else{streak=0;prefs.edit().putInt("streak",0).apply()}
     qIndex=(qIndex+1)%questions.size
    }
   }
  }
 }
}
@Composable private fun HomeScreen(score:Int,solved:Int,streak:Int,onStart:(Mode)->Unit){
 val bg=Color(0xFF090D2B); val ink=Color(0xFF29243F); val muted=Color(0xFF858097)
 Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF080D2C),Color(0xFF17134A),Color(0xFF26145A),Color(0xFF090D2B)))).verticalScroll(rememberScrollState()).padding(horizontal=18.dp, vertical=16.dp)){
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){
   Row(verticalAlignment=Alignment.CenterVertically){
    Box(Modifier.size(48.dp).background(Brush.linearGradient(listOf(Color(0xFF7657D6),Color(0xFFB28CF6))),RoundedCornerShape(16.dp)),contentAlignment=Alignment.Center){Text("🧠",fontSize=26.sp)}
    Spacer(Modifier.width(10.dp));Column{Text("تحدّي المعرفة",color=Color.White,fontSize=21.sp,fontWeight=FontWeight.ExtraBold);Text("العب بذكاء… واصنع إنجازك",color=Color(0xFFBFC9F8),fontSize=11.sp)}
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
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("اختر مغامرتك",color=Color.White,fontSize=19.sp,fontWeight=FontWeight.ExtraBold);Text("٦ أنماط",color=Color(0xFF7657D6),fontSize=11.sp,fontWeight=FontWeight.Bold)}
  Text("كل نمط يختبر مهارة مختلفة لديك",color=Color(0xFFBFC9F8),fontSize=12.sp,modifier=Modifier.padding(top=4.dp,bottom=13.dp))
  val colors=listOf(Color(0xFFFFEEE4),Color(0xFFEDE8FF),Color(0xFFE1F7F4),Color(0xFFFFF3D9),Color(0xFFE5F0FF),Color(0xFFFFE8F1))
  val emojis=listOf("🕵️","🧩","⚡","🧠","🔢","✍️")
  Mode.values().toList().chunked(2).forEachIndexed{row,group->
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(11.dp)){
    group.forEachIndexed{col,mode->
     val i=row*2+col
     Column(Modifier.weight(1f).padding(bottom=11.dp).background(Color(0xFFF8F7FF),RoundedCornerShape(21.dp)).clickable{onStart(mode)}.padding(14.dp)){
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
@Composable private fun ChallengeScreen(c:Challenge,number:Int,total:Int,score:Int,onBack:()->Unit,onSpend:(Int)->Unit,onNext:(Boolean)->Unit){
 var clueCount by remember(c){mutableIntStateOf(0)}
 var helpMessage by remember(c){mutableStateOf("")}
 var answer by remember(c){mutableStateOf("")}
 var checked by remember(c){mutableStateOf(false)}
 var revealMemory by remember(c){mutableStateOf(true)}
 val memorySequence=c.prompt.substringAfter(": ",c.prompt).substringBefore(". ")
 val memoryQuestion=c.prompt.substringAfter(". ","ما العنصر المطلوب؟")
 val correct=c.answers.any{normalize(answer)==normalize(it)}
 LaunchedEffect(c){if(c.mode==Mode.MEMORY){revealMemory=true;kotlinx.coroutines.delay(3500);revealMemory=false}}
 Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF090D2B),Color(0xFF1B1650),Color(0xFF0B1238)))).verticalScroll(rememberScrollState()).padding(18.dp)){
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){TextButton(onClick=onBack){Text("→ رجوع",color=Purple,fontWeight=FontWeight.Bold)};Spacer(Modifier.weight(1f));Text("تحدّي المعرفة",color=Color.White,fontWeight=FontWeight.ExtraBold)}
  Spacer(Modifier.height(12.dp))
  Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Purple,Color(0xFFAA83F2))),RoundedCornerShape(24.dp)).padding(18.dp)){
   Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(c.mode.title,color=Color.White,fontSize=15.sp,fontWeight=FontWeight.Bold);Text("السؤال "+number+" من "+total,color=Color.White,fontSize=12.sp)}
   Spacer(Modifier.height(13.dp));LinearProgressIndicator(progress={number.toFloat()/total.coerceAtLeast(1)},modifier=Modifier.fillMaxWidth().height(7.dp),color=Color(0xFFFFD18B),trackColor=Color(0x557FFFFFFF))
   Spacer(Modifier.height(12.dp));Text("🎯  "+c.category,color=Color.White,fontSize=12.sp)
  }
  Spacer(Modifier.height(18.dp))
  Column(Modifier.fillMaxWidth().background(Color(0xFFF9F8FF),RoundedCornerShape(23.dp)).padding(19.dp)){
   Text(if(c.mode==Mode.MEMORY&&revealMemory)"احفظ جيداً… ستختفي العناصر بعد لحظات" else "السؤال",color=Gold,fontSize=13.sp,fontWeight=FontWeight.ExtraBold)
   Spacer(Modifier.height(10.dp));Text(if(c.mode==Mode.MEMORY){if(revealMemory)memorySequence else memoryQuestion}else c.prompt,color=Ink,fontSize=21.sp,fontWeight=FontWeight.Bold,lineHeight=32.sp)
   if(c.mode==Mode.MEMORY&&revealMemory){Spacer(Modifier.height(10.dp));Text("⏳ احفظ التسلسل الآن",color=Purple,fontSize=12.sp,fontWeight=FontWeight.Bold)}
  }
  Spacer(Modifier.height(16.dp))
  Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){Row(verticalAlignment=Alignment.CenterVertically){Text("💡",fontSize=20.sp);Spacer(Modifier.width(7.dp));Text("المساعد الذكي",color=Ink,fontWeight=FontWeight.Bold,fontSize=15.sp)};Text("🪙 "+score,color=Purple,fontWeight=FontWeight.ExtraBold,fontSize=13.sp)}
  if(clueCount>0)c.clues.take(clueCount).forEach{Text("•  "+it,color=Ink,modifier=Modifier.fillMaxWidth().padding(top=9.dp).background(Color.White,RoundedCornerShape(14.dp)).padding(14.dp),fontSize=13.sp)}
  if(!checked&&clueCount<c.clues.size){Button(onClick={if(score>=5){onSpend(5);clueCount++;helpMessage="تم خصم 5 عملات وإظهار تلميح جديد."}else helpMessage="رصيدك غير كافٍ. أجب إجابة صحيحة لتحصل على 10 عملات."},modifier=Modifier.fillMaxWidth().padding(top=10.dp),shape=RoundedCornerShape(14.dp),colors=ButtonDefaults.buttonColors(containerColor=Purple)){Text("استخدم المساعدة  •  5 🪙",fontWeight=FontWeight.Bold)}}
  if(helpMessage.isNotBlank())Text(helpMessage,color=Purple,fontSize=11.sp,modifier=Modifier.padding(top=7.dp))
  if(clueCount>=c.clues.size)Text("وصلت إلى آخر تلميح لهذا السؤال.",color=Muted,fontSize=11.sp,modifier=Modifier.padding(top=7.dp))
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
