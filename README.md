# QnA-Board-spring
---
### server에 배포(AWS 사용-ubuntu)
- 배포 과정 : 코드 수정 -> git에 push -> git pull로 ubuntu에서 바뀐코드 업데이트 -> 빌드 -> 서버 실행(서버가 실행되고 있는 경우 ps -ef | grep java 명령어로 확인 가능 이과정으로 프로세스 아이디를 알아오고 kill -9 프로세스 아이디를 통해 기존 포트로 실행되고 있는 서버를 멈춘다.)
- ubuntu에 java 설치
  1. wget --header "Cookie: oraclelicense=accept-securebackup-cookie" 뒤에 jdk 링크 경로를 붙힌다.
  2. gunzip 명령어로  zip 풀기
  3. tar -xvf 로 tar파일 풀기
  4. 환경변수 설정 : .bash_profile에 환경변수를 추가한다. JAVA_HOME=/home/ubuntu/java PATH=$PATH:$JAVA_HOME/bin 추가
  encoding 한글깨짐 방지 :LANG="ko_KR.UTF-8" LAGUAGE="ko_KR:ko:en_US:en" 추가
- maven build : ./mvnw clean package ->permission denied 인 경우 chmod 775 ./mvnw 명령어 사용후 다시 빌드
- build를 한 경우 target 안에 jar 파일이 생성됨. -> 생성된 jar 파일을 java -jar 로 실행 시키면 서버를 실행할 수 있다. 마지막에 &를 추가하면 서버를 나갔도 계속 웹이 접근이 가능하도록 할 수 있다.
- 방화벽 문제가 생기는 경우 ufw 명령어참고
- linux 심볼릭 링크 사용 :  ln -s 명령어 사용
- local에서 mustache template이 잘 작동하였지만 서버로 연결하여 배포했을 때 잘 적용되지 않는다.
 -> local에서는 project가 잘 나누어져서 관리 되고 있지만 서버로 연결했을 때는 하나의 jar파일로 묶여서 관리되기 때문이다. 
  -> 이를 해결하기 위해 서버에서 하나의 jar파일로 묶어서 관리되지 않도록 하는 방버을 찾아야한다.(mvnw를 활용할 시에는 mvnw spring boot:run 명령어를 사용할 수 있다.)

---
### 회원가입, 사용자목록, 개인정보 수정
- mustache를 사용하기 위해서는 spring.mustache.suffix=.html 설정을 한 뒤 사용
- return "/user/list" 이렇게 return 을 하게되면 template 파일에 있는 /user/list.html로 이동하게된다.(이런식으로 이동을 하게될 경우 무조건 Controller를 통해서 이동해서 template파일에 접근이 가능하다.)
- mustache 문법 html에 적용
  1. {{#users}} , {{/users}} 사용으로 반복문을 사용할 수 있다. ->users에 해당하는 것은 Controller에 있는 model.addAttribute("users",userRepository.findAll());과 같은 코드를 사용하여 users에 해당하는 요소에 추가해줄 수 있다.
  2. 위의 태그 안에서 {{id}}이렇게 사용하면 users.id에 접근가능하다. 
  3. html파일에서 반복되는 코드들을 모아서 관리할 수 있다. ex){{> /include/nav}}
- 개인정보 수정시에는 해당하는 user를 찾기위한 id를 url에 포함시켜 보내야한다. 그리고 개인정보 수정시 수정한 user의 정보를 받아와야한다.
- html에서 put method를 사용하기 위해서는 spring.mvc.hiddenmethod.filter.enabled=true 로 설정하고   <input type="hidden" name="_method" value="PUT"/>를 사용한다.

---
### 로그인, 로그아웃 
 - spring.mustache.expose-session-attributes=true 을 통해서 mustache에서 session에 있는 값을 사용하게 할 수 있다.
 - 로그인, 로그아웃을 할 때는 중복되는 코드가 많기 때문에 따로 패키지나 클래스를 만들어 처리하는 것이 좋다.
 - session , cookie 등등 사용하여 구현가능
 
---
### 질문 목록, 추가, 수정, 삭제
- html action 수정 -> domain에 question +repository 추가 -> controller 생성  
- html에서 name에 "contents" 를 사용했다면 controller에서 String contents로 받을 수 있다.
- 해당하는 객체를 찾기 위해서 url에 객체에 주어진 id를 포함시켜서 얻을 수 있다.
- Question 객체와 User 객체 사이의 관계 매핑 : Question의 writer가 userId이기 떄문에 관게를 매핑시킨다. 
  Question은 User 하나에 여러개 일 수 있기 때문에 ManyToOne으로 매핑한다. @JoinColumn(foreignKey = @ForeignKey(name="fk_question_writer")) DB 외래키 이름을 다음과 같이 설정할 수 있다.
- 에러 처리: 에러처리를 하는 메세지를 사용자에게 보여주기 위한 방법으로 각각의 에러를 발생 시킨 후 try catch 를 사용하는 방법이 있다. -> catch문에서 에러메세지를 받아서 화면으로(html)로 전달
- 하나의 question에 해당하는 여러 answer를 받기 위해 OneToMany로 매핑을 한다. 이 떄  @OneToMany(mappedBy = "question")를 사용해야한다. mappedBy에 해당하는 것은 answer class에서 ManyToOne으로 매핑한 Question 객체의 이름이다.
    @OrderBy("id ASC")의 사용으로 정렬순서를 지정할 수 있다.
- @Lob 사용: content의 길이를 많이 쓸 수 있도록 하기 위함.
- 날짜 설정 : createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss")); 다음과같은 방식으로 사용가능.(createDate은 LocalDateTime자료형의 변수)
- DB에서 날짜를 TimeStamp형으로 바꾸기 : @Converter 사용 

---
### 답변 추가,삭제 ajax 사용
- $(".answer-write input[type=submit]").click(addAnswer); click을 했을 때 addaAnswer함수를 호출하도록 작성
- e.preventDefault(); 클릭을 했을 때 자동으로 submit 되는 것을 막는다.
- var queryString =$(".answer-write").serialize(); 다음과 같이 serialize 함수를 사용하여 데이터를 가져올 수 있다.
- var url= $(".answer-write").attr("action"); action에 해당하는 url을 가져온다.
- $.ajax({type : 'post',
            url : url,
            data : queryString,
            dataType : 'json',
            error : onError,
            success : onSuccess}); 를 사용하여 ajax가 작동하도록 한다. (위에서 가져온 정보들을 사용한다.)
- var answerTemplate=$("#answerTemplate").html();
      var template=answerTemplate.format(data.writer.userId,
      data.formattedCreateDate,
      data.content,
      data.id,
      data.id);
      $(".qna-comment-slipp-articles").prepend(template); ajax를 통해 다시 생기는 html template 을 가져와서 format을 사용하여 데이터를 넣어준다.(데이터가 들어가는 곳은 template 파일에 {0},{1}... 에 해당하는 부분이다.)
      지정한 데이터를 prepend함수를 사용하여 적용시킨다.
- $(document).on('click', '.link-delete-answer', deleteAnswer); 이렇게 사용해야 type=delete가 적용되어 controller에 매핑되어있는 delete에 올바르게 갈 수 있다.
- success : function (data, status)를 사용하면 data를 통해서 return 시킨 데이터를 얻어올 수 있다.
- json 데이터를 올바르게 전달하기 위해서 domain 패키지에서 entity에 해당하는 객체들의 데이터에 json 데이터로 사용할 컬럼들은 @JsonProperty 를 사용한다.
- Result 클래스 생성 : ajax 부분에서 success,error 부분을 나누기 위해 성공했을 때 데이터를 알아보기위해서 성공했을 경우 Result.ok 와 같이 사용할 수 있도록 한다.
