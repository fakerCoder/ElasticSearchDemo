package com.example.demo.controller;

import com.example.demo.dao.BlogDao;
import com.example.demo.dao.DemoDao;
import com.example.demo.entity.BlogEntity;
import com.example.demo.entity.DemoEntity;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RequestMapping("/demo")
@RestController
public class DemoController {

    @Autowired
    private DemoDao demoDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private ElasticsearchTemplate template;
    /**
     * template
     *
     * @return
     */
    @RequestMapping("/template")
    public ResponseEntity<Iterable> template() {

        template.createIndex(DemoEntity.class);


        List<DemoEntity> list = new ArrayList<>();
        demoDao.saveAll(list);


        return new ResponseEntity<>( HttpStatus.OK);
    }

    /**
     * 插入数据，查询所有
     *
     * @return
     */
    @RequestMapping("/index")
    public ResponseEntity<Iterable> index() {
//        for (int i = 0; i < 10; i++) {
//            DemoEntity post = new DemoEntity();
//            post.setTitle(getTitle().get(i));
//            post.setContent(getContent().get(i)+",demoname");
//            post.setName(getName() );
//            post.setDesc(getDesc());
//            post.setAge((i&10)*2);
//            demoDao.save(post);
//        }
        List<BlogEntity> list = new ArrayList<>();
        BlogEntity post1 = new BlogEntity();
        post1.setId("0XSl6G4BWK5G0xRN_D67");
        post1.setTitle("3" );
        post1.setContent("3" );
        post1.setAuthor("3");
        list.add(post1);
        BlogEntity post = new BlogEntity();
        post.setId("0nSl6G4BWK5G0xRN_D67");
        post.setTitle("4" );
        post.setContent("4" );
        post.setAuthor("4");
        list.add(post);
        blogDao.saveAll(list);


        Iterable<BlogEntity> list1 = blogDao.findAll();

        return new ResponseEntity<Iterable>(list1, HttpStatus.OK);
    }

    /**
     * 根据内容查询，dao里面的写法支持提示
     *
     * @param content
     * @return
     */
    @RequestMapping(value = "/{content}", method = RequestMethod.GET)
    public ResponseEntity<List<DemoEntity>> getByName(@PathVariable("content") String content) {
        List<DemoEntity> book = demoDao.findDemoEntitiesByContent(content);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }


    /**
     * 不分字段的查询内容，匹配所有字段
     *
     * @param param
     * @return
     */
    @GetMapping("/select/{param}")
    public ResponseEntity<List<DemoEntity>> testSearch(@PathVariable String param) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(param);
        Iterable<DemoEntity> searchResult = demoDao.search(builder);
        Iterator<DemoEntity> iterator = searchResult.iterator();
        List<DemoEntity> list = new ArrayList<DemoEntity>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 删除所有，支持单个删除
     *
     * @return
     */
    @GetMapping("/delete")
    public ResponseEntity<List<DemoEntity>> delete() {
        demoDao.deleteAll();

        System.out.println("*****************");
        blogDao.deleteAll();
        System.out.println("===========");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @param query
     * @return
     */
    @GetMapping("/{page}/{size}/{query}")
    public List<DemoEntity> searchCity(@PathVariable Integer page, @PathVariable Integer size, @PathVariable String query) {
        Sort sort =  Sort.by(Sort.Direction.ASC, "age");
        Pageable pageable = PageRequest.of(page, size, sort);
        // 分数、分页
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("content",query);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(queryBuilder).withIndices("demoname","blogname").build();
        Page<DemoEntity> searchPageResults = demoDao.search(searchQuery);

        return searchPageResults.getContent();
    }

    private List<String> getContent() {
        List<String> list = new ArrayList<>();
        list.add("初中 宋·李清照 常记溪亭日暮误入藕花深处。争渡，争渡");
        list.add("重阳节 宋·李佳节又重阳，玉枕纱厨，半夜凉初透。东");
        list.add("闺怨诗清照 寻寻觅觅，乍暖还寒时候，最难将息。三杯两");
        list.add("元宵节 宋·李清云染柳烟浓，吹梅笛怨，春意知几许。元");
        list.add("婉约诗 试问卷帘人，却道海棠依旧。知否，知否");
        list.add("描写梅花香脸半开娇旖旎，当庭际，玉人浴出");
        list.add(" 宋·李清露浓花瘦，薄汗轻衣透。见客入来，袜刬金");
        list.add("闺怨诗 宋·1李清照 几点催花雨。倚遍阑干，只是无");
        list.add("婉约诗 阳关，唱到千千遍。人道山长水又断。萧萧微雨闻");
        list.add("描写春天 宋·李清照 暖雨晴风腮，酒意诗情谁与共？泪融残粉花");
        list.add("寒食节，梦回山枕隐花钿。海燕未来人斗草，");
        list.add(" 宋·李清照 髻子伤春慵更梳，玉鸭薰炉闲瑞脑，");
        list.add(" 宋·李融。疏钟已应晚来风。瑞脑香消魂梦断，");
        list.add("闺怨诗 宋·李清照 小院闲窗春已深，，远岫出山催薄暮。");
        list.add("爱情诗 宋·李清 绣幕芙蓉斜偎宝鸭亲香腮。一面风情深有韵，");
        list.add("描写春春欲放。泪染轻匀，怕郎猜道，奴");
        list.add("》 宋·李3清照 用其语作“庭");
        list.add("描写梅花 宋·损芳姿。夜来清梦好，应是发");
        list.add("寒食节 宋·李清照 萧条庭院闭。宠柳娇花寒食。险");
        list.add("思乡乍著心情好。睡起觉微寒，梅花鬓上残。故乡何处");
        list.add("描写春天 宋钗头人胜轻。角声催晓");
        list.add("闺李清照 风住，日晚倦梳头。事休，欲语泪先流。闻说双溪");
        list.add(" 宋·李清照 云中谁寄锦书来？雁字回时，月");
        list.add("豪放诗 仿佛梦魂归帝所。闻天语。殷勤问我");
        list.add("李清照 暗淡轻黄体性柔自是花中第一流。");
        list.add("描写秋天 宋·李梧桐应恨夜来霜。酒阑，梦断偏宜瑞脑香。");
        list.add("闺怨诗 宋·解罗裳，独上兰舟。雁字回时，月");
        list.add(" 宋1·李清照 误入藕花深处。争渡。争渡");
        list.add(" 宋·融。已应晚来风。瑞脑香消魂梦断，");
        list.add(" 宋。倚楼无语理瑶琴。远岫出山催薄暮，");
        list.add(" 宋。海燕未来人斗草，");
        list.add(" 。四叠阳关，唱到千千遍。人道山长山又断。萧萧微雨闻");
        list.add(" 已觉春心动。酒意诗情谁与共。泪融残粉花");
        list.add(" 萧上酒阑更喜团茶苦，梦断偏宜瑞脑香。");
        list.add(" 宋·重阳，玉枕纱厨，半夜凉初透。东");
        list.add(" 宋何须浅碧深红色，自是花中第一流。");
        list.add("清照 永夜恹恹欢意少。空梦长安，花光月影宜");
        list.add(" 宋·李照 髻子伤春慵更梳淡云来往月疏疏。玉鸭熏炉闲瑞脑，");
        list.add("绣面芙蓉一笑开。斜飞宝鸭一面风情深有韵，");
        list.add("谁伴明清窗独坐，我共影儿俩个无那，无那");
        return list;
    }

    private List<String> getTitle() {
        List<String> list = new ArrayList<>();
        list.add("《如梦令》");
        list.add("《醉花阴》");
        list.add("《声声慢》");
        list.add("《永遇乐》");
        list.add("《昨夜乐昼风骤》");
        list.add("《雪日溪沙暮至》");
        list.add("《蹴[1]罢秋千》");
        list.add("《点绛唇》");
        list.add("《蝶恋花》");
        list.add("《离情》");
        list.add("《浣溪沙1》");
        list.add("《浣2》");
        list.add("《浣3溪沙3》");
        list.add("《浣溪沙日暮4》");
        list.add("《浣溪沙5》");
        list.add("《减字木兰花》");
        list.add("《欧阳公作");
        list.add("《庭院已尽深几许》");
        list.add("《念奴娇》");
        list.add("《风柔日薄春犹早》");
        list.add("《菩萨蛮》");
        list.add("《风住尘香花》");
        list.add("《一剪梅》");
        list.add("《渔家傲》");
        list.add("《鹧乐鸪天》");
        list.add("《鹧鸪》");
        list.add("《剪梅》");
        list.add("《如梦暮》");
        list.add("《浣溪6沙》");
        list.add("《浣已尽溪7沙》");
        list.add("《浣溪8沙》");
        list.add("《蝶恋粉满》");
        list.add("《暖日萧萧晴》");
        list.add("《鹧鸪锁窗》");
        list.add("《醉花阴》");
        list.add("《鹧鸪天·暗淡轻》");
        list.add("《蝶恋萧萧花》");
        list.add("《9浣溪沙》");
        list.add("《10浣溪沙》");
        list.add("《如梦令独坐》");
        return list;
    }

    private String getDesc(){
        Random random = new Random();
        String[] descArr = {"大家好，我是雨天，因为刚好出生在雨天，一分为二成羽立了。"
                , "大家都说我是个阳光女孩，因为我是开心果啊。我老是坐不住，能跟小椅子成为好朋友"
                , "不用按老师的要求练基本功；我喜欢画画，但最好是信手涂鸦，"
                , "把小朋友的脸画成绿色也没关系；我喜欢溜冰，但最有趣的还是一起练习如何摔跤……"
                , "昀昀是我在妈妈肚子里时的小名哦，我的大名叫曹铁瀛，妈妈怀我的时候缘故，"
                , "干脆就叫“天赢”好了，爸妈取其谐音，就变成了“铁瀛”"
                , "嘿！我——21号来报到了！智诰、诰诰、阿诰，你们爱怎么叫就怎么叫吧！一名先生取的"
                , "还是汽车发烧友，小轿车、大卡车、集装箱、大客车、翻斗车就差拖拉机还没有报到）。"
                , "我常在家练习倒车、移库、爬坡、过单轨桥，考取驾照不成问题深沟，追尾事故，"
                , "因为那时我又能大显身手，汽车的零件装了又拆，拆了又装……哈哈！！"
                ,"角角落落都是我的杰作，缺胳膊少腿的，七零八落的，尽管汽车多，，又有理由去买车啦！"
                , "我还喜欢画画。从小我就拿着笔画一个个的大圆圈，，就是那样练出来的哦！",
                "以后我一定会继续努力的！可是我不太喜欢看书常常惹她不高兴，可我就是不喜欢嘛！"
                , "我的性格有点内向、腼腆、不喜欢“显山露水”。乒乓球、羽毛球、台球、（这可是遗传）。"
                , "我的个子很高，被称为“帅哥”，，其中三个为：外婆、爸爸和妈妈。 我的目标是考上北大。"
                , "我当时的第一反应是：这两个字可以跟名人的名字做一个联系，然后候可以跟别人讲："
        };
        int i = random.nextInt(descArr.length);
        return descArr[i];
    }
    private String getName() {
        String namess = "帆乘   楷栋   锋枫   海勇   康帆   安礼   晓平   良帆   瑞翱   涛锟   恒勇   鸿驰   帆强   " +
                "桓柏   锋寅   博槐   骞琛   桓钊   杰桓   裕枫   福晖   槐仕   奇鹏   骏伟   允潍   乘初   杞郁   "
                ;
        Random random = new Random();

        String[] names = namess.split("\\s+");
        int i = random.nextInt(names.length);
        return names[i];
    }
}
