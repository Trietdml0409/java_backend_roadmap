# Ngày 15-16: Java Streams API

> **Thời lượng:** ~6 giờ
> **Mục tiêu:** Nắm vững Stream API — từ khái niệm pipeline, intermediate/terminal operations, đến Collectors nâng cao và parallel streams
> **Kiến thức nền:** Bạn đã biết Collections (Day 8-9), Generics (Day 10-11), Lambda Expressions & Functional Interfaces (Day 12-13)

---

## 1. Stream Pipeline — Khái Niệm Cốt Lõi

### 1.1 Stream là gì?

Stream là một **chuỗi các phép biến đổi dữ liệu** (pipeline) trên một nguồn dữ liệu (collection, array, file, ...). Stream **không phải** là cấu trúc dữ liệu — nó **không lưu trữ** phần tử mà chỉ truyền tải và biến đổi chúng.

Nếu bạn quen với JavaScript, bạn hay viết:

```javascript
// JS — mỗi method tạo array mới và chạy NGAY LẬP TỨC
const result = users
    .filter(u => u.age >= 18)
    .map(u => u.name)
    .sort();

```

Hoặc trong Python:

```python
# Python — list comprehension chạy ngay lập tức
result = sorted([u.name for u in users if u.age >= 18])
```

Java Streams trông tương tự nhưng có **sự khác biệt quan trọng**:

```java
// Java — KHÔNG chạy cho đến khi gặp terminal operation (.collect)
List<String> result = users.stream()
    .filter(u -> u.getAge() >= 18)    // intermediate (lazy)
    .map(User::getName)               // intermediate (lazy)
    .sorted()                         // intermediate (lazy)
    .collect(Collectors.toList());    // terminal → KÍch hoạt toàn bộ pipeline
```

### 1.2 ASCII Diagram — Stream Pipeline

```
  ┌─────────────────────────────────────────────────────────────────────┐
  │                        STREAM PIPELINE                             │
  │                                                                     │
  │  ┌──────────┐    ┌─────────────────────────────┐    ┌───────────┐  │
  │  │  SOURCE   │───▶│   INTERMEDIATE OPERATIONS   │───▶│ TERMINAL  │  │
  │  │          │    │        (0 hoặc nhiều)        │    │ OPERATION │  │
  │  └──────────┘    └─────────────────────────────┘    └───────────┘  │
  │                                                                     │
  │  Collection       filter, map, flatMap,              collect,       │
  │  Array            sorted, distinct, peek,            forEach,      │
  │  Stream.of()      limit, skip                        reduce,       │
  │  Files.lines()                                       count,        │
  │  IntStream.range()   ▲                               findFirst,    │
  │                      │                               toList()      │
  │                      │                                              │
  │                   LAZY!                           EAGER!            │
  │              Không chạy cho đến               Kích hoạt toàn bộ    │
  │              khi có terminal op               pipeline và trả      │
  │                                               về kết quả           │
  └─────────────────────────────────────────────────────────────────────┘

  Luồng dữ liệu (với ví dụ cụ thể):

  [User("An",25), User("Bình",16), User("Cường",30), User("Dũng",17)]
       │
       ▼  .stream()
  Stream<User>
       │
       ▼  .filter(u -> u.getAge() >= 18)        ← loại bỏ Bình(16), Dũng(17)
  Stream<User>  → [User("An",25), User("Cường",30)]
       │
       ▼  .map(User::getName)                   ← trích xuất tên
  Stream<String> → ["An", "Cường"]
       │
       ▼  .sorted()                             ← sắp xếp
  Stream<String> → ["An", "Cường"]
       │
       ▼  .collect(Collectors.toList())          ← THU THẬP kết quả
  List<String>   → ["An", "Cường"]
```

### 1.3 Ba Quy Tắc Quan Trọng

#### Quy tắc 1: Streams là LAZY (Lười Biếng)

```java
// Không có gì xảy ra ở dòng này — chỉ tạo "kế hoạch" thực thi
Stream<User> filtered = users.stream()
    .filter(u -> {
        System.out.println("Filtering: " + u.getName());
        return u.getAge() >= 18;
    })
    .map(u -> {
        System.out.println("Mapping: " + u.getName());
        return u.getName();
    });


System.out.println("Chưa có output nào ở trên!");

// BÂY GIỜ mới chạy — khi gặp terminal operation
List<String> result = filtered.collect(Collectors.toList());
// Output:
// Chưa có output nào ở trên!
// Filtering: An
// Mapping: An
// Filtering: Bình
// Filtering: Cường
// Mapping: Cường
```

**So sánh với JS:** Trong JavaScript, `.filter()` và `.map()` chạy **ngay lập tức** và tạo array trung gian. Java Stream **không tạo collection trung gian** — mỗi phần tử đi qua toàn bộ pipeline trước khi phần tử tiếp theo bắt đầu.


| Đặc điểm              | Java Streams               | JS Array methods                               | Python list comprehension |
| --------------------- | -------------------------- | ---------------------------------------------- | ------------------------- |
| Thời điểm chạy        | Lazy (khi gặp terminal op) | Eager (chạy ngay)                              | Eager (chạy ngay)         |
| Collection trung gian | Không tạo                  | Tạo array mới mỗi bước                         | Tạo list mới              |
| Short-circuit         | Có (findFirst, limit...)   | Không (.find() có nhưng .filter().map() không) | Generator thì có          |


#### Quy tắc 2: Stream KHÔNG thay đổi source collection

```java
List<Integer> numbers = new ArrayList<>(List.of(3, 1, 4, 1, 5));

List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());

System.out.println(numbers); // [3, 1, 4, 1, 5] — KHÔNG ĐỔI
System.out.println(sorted);  // [1, 1, 3, 4, 5] — list mới
```

#### Quy tắc 3: Một Stream chỉ dùng được MỘT LẦN

```java
Stream<String> stream = List.of("a", "b", "c").stream();

stream.forEach(System.out::println); // OK — lần đầu

stream.forEach(System.out::println); // IllegalStateException!
// "stream has already been operated upon or closed"
```

Đây là điểm khác biệt lớn so với Python generators (cũng dùng một lần) nhưng khác với JS arrays (dùng lại thoải mái):


|          | Java Stream | Python Generator | JS Array |
| -------- | ----------- | ---------------- | -------- |
| Dùng lại | Không       | Không            | Có       |
| Lazy     | Có          | Có               | Không    |


---

## 2. Intermediate Operations (Phép Toán Trung Gian — Lazy)

Tất cả intermediate operations đều **trả về một Stream mới** và **không chạy cho đến khi gặp terminal operation**.

### 2.1 filter(PredicateT)

Giữ lại các phần tử thoả mãn điều kiện.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// [2, 4, 6, 8, 10]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   list.stream().filter(x -> x > 5).collect(toList())      │
│ JS/TS:  list.filter(x => x > 5)                                 │
│ Python: [x for x in list if x > 5]                              │
└──────────────────────────────────────────────────────────────────┘
```

### 2.2 map(FunctionT, R)

Biến đổi mỗi phần tử từ kiểu T sang kiểu R.

```java
List<String> names = List.of("an", "bình", "cường");

List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// ["AN", "BÌNH", "CƯỜNG"]

// map cũng có thể thay đổi kiểu
List<Integer> lengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList());
// [2, 4, 6]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   list.stream().map(s -> s.toUpperCase()).collect(toList())│
│ JS/TS:  list.map(s => s.toUpperCase())                          │
│ Python: [s.upper() for s in list]                                │
└──────────────────────────────────────────────────────────────────┘
```

### 2.3 flatMap(FunctionT, StreamR)

"Phẳng hóa" (flatten) các stream lồng nhau. Chi tiết ở Phần 6.

```java
List<List<String>> nested = List.of(
    List.of("a", "b"),
    List.of("c", "d"),
    List.of("e")
);

List<String> flat = nested.stream()
    .flatMap(Collection::stream)    // List<String> → Stream<String>
    .collect(Collectors.toList());
// ["a", "b", "c", "d", "e"]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   nested.stream().flatMap(Collection::stream)              │
│ JS/TS:  nested.flat()  hoặc  nested.flatMap(x => x)             │
│ Python: [item for sublist in nested for item in sublist]         │
└──────────────────────────────────────────────────────────────────┘
```

### 2.4 sorted() và sorted(ComparatorT)

Sắp xếp các phần tử.

```java
List<String> names = List.of("Cường", "An", "Bình");

// Sắp xếp tự nhiên (natural order)
List<String> sorted1 = names.stream()
    .sorted()
    .collect(Collectors.toList());
// ["An", "Bình", "Cường"]

// Sắp xếp theo độ dài tên
List<String> sorted2 = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
// ["An", "Bình", "Cường"]

// Sắp xếp ngược
List<String> sorted3 = names.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
// ["Cường", "Bình", "An"]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────────┐
│ Java:   stream.sorted(Comparator.comparingInt(String::length))       │
│ JS/TS:  arr.sort((a, b) => a.length - b.length)  ← MUTATES array!   │
│ Python: sorted(lst, key=len)                      ← trả về list mới  │
└──────────────────────────────────────────────────────────────────────┘
```

**Lưu ý:** JS `.sort()` thay đổi array gốc! Java Stream `.sorted()` thì không.

### 2.5 distinct()

Loại bỏ phần tử trùng lặp (dựa trên `.equals()`).

```java
List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4);

List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// [1, 2, 3, 4]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.distinct().collect(toList())                      │
│ JS/TS:  [...new Set(arr)]                                        │
│ Python: list(set(lst))     ← mất thứ tự!                        │
│         list(dict.fromkeys(lst))  ← giữ thứ tự                  │
└──────────────────────────────────────────────────────────────────┘
```

### 2.6 peek(ConsumerT)

Thực hiện side-effect (thường là logging/debug) mà **không thay đổi** phần tử. Dùng để debug pipeline.

```java
List<String> result = List.of("one", "two", "three", "four").stream()
    .filter(s -> s.length() > 3)
    .peek(s -> System.out.println("Sau filter: " + s))
    .map(String::toUpperCase)
    .peek(s -> System.out.println("Sau map: " + s))
    .collect(Collectors.toList());
// Output:
// Sau filter: three
// Sau map: THREE
// Sau filter: four
// Sau map: FOUR
// result = ["THREE", "FOUR"]
```

**Cảnh báo:** `peek()` chỉ dùng để **debug**. Không nên dùng cho business logic vì behavior có thể thay đổi tùy implementation (đặc biệt với parallel streams).

### 2.7 limit(long n) và skip(long n)

page: 3 perPage = 10. 100 phần tử (products) - pagination

`limit(n)` — lấy tối đa n phần tử đầu tiên. 10  
`skip(n)` — bỏ qua n phần tử đầu tiên. 20 phần tử

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Lấy 3 phần tử đầu
List<Integer> first3 = numbers.stream()
    .limit(3)
    .collect(Collectors.toList());
// [1, 2, 3]

// Bỏ qua 3 phần tử đầu
List<Integer> skipFirst3 = numbers.stream()
    .skip(3)
    .collect(Collectors.toList());
// [4, 5, 6, 7, 8, 9, 10]

// Phân trang (pagination) — lấy trang 2, mỗi trang 3 phần tử
int page = 2;
int pageSize = 3;
List<Integer> page2 = numbers.stream()
    .skip((long)(page - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());
// [4, 5, 6]
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.skip(3).limit(3)                                  │
│ JS/TS:  arr.slice(3, 6)                                          │
│ Python: lst[3:6]                                                 │
└──────────────────────────────────────────────────────────────────┘
```

### 2.8 Bảng Tổng Hợp Intermediate Operations


| Operation            | Kiểu          | Mô tả                      | Ví dụ                                |
| -------------------- | ------------- | -------------------------- | ------------------------------------ |
| `filter(Predicate)`  | Stateless     | Lọc phần tử theo điều kiện | `.filter(x -> x > 0)`                |
| `map(Function)`      | Stateless     | Biến đổi mỗi phần tử       | `.map(String::toUpperCase)`          |
| `flatMap(Function)`  | Stateless     | Phẳng hóa stream lồng nhau | `.flatMap(Collection::stream)`       |
| `sorted()`           | Stateful      | Sắp xếp (natural order)    | `.sorted()`                          |
| `sorted(Comparator)` | Stateful      | Sắp xếp theo comparator    | `.sorted(Comparator.reverseOrder())` |
| `distinct()`         | Stateful      | Loại bỏ trùng lặp          | `.distinct()`                        |
| `peek(Consumer)`     | Stateless     | Side-effect (debug)        | `.peek(System.out::println)`         |
| `limit(long)`        | Short-circuit | Lấy tối đa n phần tử       | `.limit(5)`                          |
| `skip(long)`         | Stateless     | Bỏ qua n phần tử đầu       | `.skip(3)`                           |


**Stateless vs Stateful:**

- **Stateless:** Xử lý từng phần tử độc lập (filter, map). Hiệu quả hơn với parallel streams.
- **Stateful:** Cần "nhớ" trạng thái (sorted cần xem tất cả phần tử, distinct cần nhớ đã thấy gì). Có thể chậm hơn với parallel streams.

---

## 3. Terminal Operations (Phép Toán Kết Thúc — Eager)

Terminal operations **kích hoạt toàn bộ pipeline** và trả về kết quả (không phải Stream). Sau khi terminal operation chạy, stream **không thể dùng lại**.

### 3.1 collect(Collector)

Thu thập kết quả vào một collection hoặc cấu trúc dữ liệu. Đây là terminal operation **phổ biến nhất**. Chi tiết về Collectors ở Phần 4.

```java
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());
Map<String, Integer> map = stream.collect(Collectors.toMap(
    User::getName,
    User::getAge
));
```

### 3.2 toList() — Java 16+

Phiên bản ngắn gọn hơn của `collect(Collectors.toList())`. Trả về **unmodifiable list**.

```java
// Java 16+
List<String> names = users.stream()
    .map(User::getName)
    .toList();  // ngắn gọn hơn .collect(Collectors.toList())

// Lưu ý: list trả về là UNMODIFIABLE
names.add("New");  // UnsupportedOperationException!
```


|                  | `collect(Collectors.toList())` | `.toList()` (Java 16+)        |
| ---------------- | ------------------------------ | ----------------------------- |
| Kết quả          | Mutable ArrayList              | Unmodifiable List             |
| Thêm/xóa phần tử | Được                           | UnsupportedOperationException |
| Null elements    | Cho phép                       | Cho phép                      |


### 3.3 forEach(ConsumerT)

Thực hiện hành động trên mỗi phần tử. **Không trả về giá trị** (void).

```java
// In ra từng phần tử
names.stream().forEach(System.out::println);

// Hoặc ngắn gọn hơn — dùng trực tiếp trên collection
names.forEach(System.out::println);  // không cần .stream()
```

**Lưu ý:** `forEach` trên Stream khác với `forEach` trên Collection. Stream `forEach` **không đảm bảo thứ tự** khi dùng parallel stream. Dùng `forEachOrdered` nếu cần giữ thứ tự.

### 3.4 reduce() — Chi Tiết Ở Phần 5

Gộp tất cả phần tử thành một giá trị duy nhất.

```java
// Tính tổng
int sum = List.of(1, 2, 3, 4, 5).stream()
    .reduce(0, Integer::sum);
// 15
```

### 3.5 count()

Đếm số phần tử trong stream.

```java
long count = List.of(1, 2, 3, 4, 5).stream()
    .filter(n -> n > 3)
    .count();
// 2
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.filter(x -> x > 3).count()                       │
│ JS/TS:  arr.filter(x => x > 3).length                           │
│ Python: len([x for x in lst if x > 3])                          │
│         hoặc: sum(1 for x in lst if x > 3)  ← tối ưu bộ nhớ    │
└──────────────────────────────────────────────────────────────────┘
```

### 3.6 findFirst() và findAny()

Tìm phần tử — trả về `Optional<T>`.

```java
Optional<String> first = List.of("alpha", "beta", "gamma").stream()
    .filter(s -> s.startsWith("b"))
    .findFirst();
// Optional["beta"]

first.ifPresent(System.out::println); // "beta"
```


|                   | `findFirst()`                | `findAny()`                      |
| ----------------- | ---------------------------- | -------------------------------- |
| Kết quả           | Phần tử đầu tiên theo thứ tự | Bất kỳ phần tử nào               |
| Sequential stream | Giống nhau                   | Giống nhau                       |
| Parallel stream   | Luôn trả về phần tử đầu tiên | Có thể trả về bất kỳ (nhanh hơn) |
| Khi nào dùng      | Cần kết quả xác định         | Chỉ cần biết có tồn tại          |


**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.filter(x -> x > 3).findFirst()   → Optional<T>   │
│ JS/TS:  arr.find(x => x > 3)                    → T | undefined  │
│ Python: next((x for x in lst if x > 3), None)   → T | None       │
└──────────────────────────────────────────────────────────────────┘
```

### 3.7 anyMatch(), allMatch(), noneMatch()

Kiểm tra điều kiện trên stream — trả về `boolean`. Đây là **short-circuit operations** (dừng sớm khi biết kết quả).

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
// true — dừng ngay khi tìm thấy 2

boolean allPositive = numbers.stream().allMatch(n -> n > 0);
// true — phải kiểm tra hết

boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
// true — phải kiểm tra hết
```


| Operation              | Mô tả                                | Short-circuit khi                     |
| ---------------------- | ------------------------------------ | ------------------------------------- |
| `anyMatch(predicate)`  | Có **ít nhất một** phần tử thoả mãn? | Tìm thấy phần tử thoả → `true`        |
| `allMatch(predicate)`  | **Tất cả** phần tử đều thoả mãn?     | Tìm thấy phần tử không thoả → `false` |
| `noneMatch(predicate)` | **Không có** phần tử nào thoả mãn?   | Tìm thấy phần tử thoả → `false`       |


**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.anyMatch(x -> x > 3)                             │
│ JS/TS:  arr.some(x => x > 3)                                    │
│ Python: any(x > 3 for x in lst)                                 │
│                                                                  │
│ Java:   stream.allMatch(x -> x > 0)                             │
│ JS/TS:  arr.every(x => x > 0)                                   │
│ Python: all(x > 0 for x in lst)                                 │
└──────────────────────────────────────────────────────────────────┘
```

### 3.8 min() và max()

Tìm phần tử nhỏ nhất / lớn nhất — trả về `Optional<T>`.

```java
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

Optional<Integer> min = numbers.stream().min(Comparator.naturalOrder());
// Optional[1]

Optional<Integer> max = numbers.stream().max(Comparator.naturalOrder());
// Optional[9]

// Với object — tìm user trẻ nhất
Optional<User> youngest = users.stream()
    .min(Comparator.comparingInt(User::getAge));
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.min(Comparator.naturalOrder())  → Optional<T>    │
│ JS/TS:  Math.min(...arr)                       → number          │
│ Python: min(lst)                               → T               │
└──────────────────────────────────────────────────────────────────┘
```

### 3.9 Bảng Tổng Hợp Terminal Operations


| Operation                       | Kiểu trả về   | Short-circuit | Mô tả                            |
| ------------------------------- | ------------- | ------------- | -------------------------------- |
| `collect(Collector)`            | R             | Không         | Thu thập vào collection/cấu trúc |
| `toList()` (Java 16+)           | `List<T>`     | Không         | Thu thập vào unmodifiable list   |
| `forEach(Consumer)`             | `void`        | Không         | Thực hiện action lên mỗi phần tử |
| `reduce(identity, accumulator)` | T             | Không         | Gộp thành một giá trị            |
| `count()`                       | `long`        | Không         | Đếm số phần tử                   |
| `findFirst()`                   | `Optional<T>` | Có            | Phần tử đầu tiên                 |
| `findAny()`                     | `Optional<T>` | Có            | Bất kỳ phần tử nào               |
| `anyMatch(Predicate)`           | `boolean`     | Có            | Ít nhất một thoả mãn?            |
| `allMatch(Predicate)`           | `boolean`     | Có            | Tất cả thoả mãn?                 |
| `noneMatch(Predicate)`          | `boolean`     | Có            | Không có phần tử nào thoả mãn?   |
| `min(Comparator)`               | `Optional<T>` | Không         | Phần tử nhỏ nhất                 |
| `max(Comparator)`               | `Optional<T>` | Không         | Phần tử lớn nhất                 |


---

## 4. Collectors Deep Dive

`Collectors` là utility class cung cấp các **collector sẵn có** để dùng với `.collect()`. Import: `import java.util.stream.Collectors;`

### 4.1 Collectors cơ bản: toList(), toSet(), toMap()

```java
import java.util.stream.Collectors;

List<String> names = List.of("An", "Bình", "Cường", "An");

// toList() — cho phép trùng lặp, có thứ tự
List<String> list = names.stream()
    .collect(Collectors.toList());
// ["An", "Bình", "Cường", "An"]

// toSet() — loại bỏ trùng lặp
Set<String> set = names.stream()
    .collect(Collectors.toSet());
// ["An", "Bình", "Cường"] — thứ tự không đảm bảo

// toUnmodifiableList() — không thể thay đổi sau khi tạo
List<String> immutable = names.stream()
    .collect(Collectors.toUnmodifiableList());
// immutable.add("Dũng") → UnsupportedOperationException
```

### 4.2 toMap() — Chuyển thành Map

```java
record User(String name, int age) {}

List<User> users = List.of(
    new User("An", 25),
    new User("Bình", 30),
    new User("Cường", 22)
);

// Cơ bản: name → age
Map<String, Integer> nameToAge = users.stream()
    .collect(Collectors.toMap(
        User::name,    // keyMapper
        User::age      // valueMapper
    ));
// {An=25, Bình=30, Cường=22} => Map
// HashMap new_key = hashFunc(key) . {A = 25, B = 30, C = 22}


// Nếu có key trùng → ném IllegalStateException!
// Giải quyết bằng mergeFunction:
List<User> withDuplicates = List.of(
    new User("An", 25),
    new User("An", 30),  // trùng key "An",
    new User("An", 27)
);

Map<String, Integer> resolved = withDuplicates.stream()
    .collect(Collectors.toMap(
        User::name,
        User::age,
        (existing, replacement) -> existing  // giữ giá trị cũ khi trùng key
    ));
// {An=25}

// Chọn implementation cụ thể (TreeMap thay vì HashMap)
Map<String, Integer> treeMap = users.stream()
    .collect(Collectors.toMap(
        User::name,
        User::age,
        (a, b) -> a,
        TreeMap::new    // mapFactory
    ));
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────────┐
│ Java:   stream.collect(Collectors.toMap(User::name, User::age))      │
│ JS/TS:  Object.fromEntries(arr.map(u => [u.name, u.age]))           │
│ Python: {u.name: u.age for u in users}                               │
└──────────────────────────────────────────────────────────────────────┘
```

### 4.3 groupingBy() — Nhóm Phần Tử

Đây là một trong những Collectors **mạnh nhất**. Tương đương SQL `GROUP BY`.

```java
record Student(String name, String major, int grade) {}

List<Student> students = List.of(
    new Student("An", "CS", 85),
    new Student("Bình", "CS", 90),
    new Student("Cường", "Math", 78),
    new Student("Dũng", "Math", 92),
    new Student("Em", "CS", 88)
);

// Nhóm theo ngành học
Map<String, List<Student>> byMajor = students.stream()
    .collect(Collectors.groupingBy(Student::major));
// {
//   CS   → [An(85), Bình(90), Em(88)],
//   Math → [Cường(78), Dũng(92)]
// }
```

#### groupingBy + downstream collector

Thay vì nhóm thành `List`, bạn có thể chỉ định **downstream collector** để xử lý tiếp mỗi nhóm:

```java
// Đếm số sinh viên mỗi ngành
Map<String, Long> countByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.counting()
    ));
// {CS=3, Math=2}

// Điểm trung bình mỗi ngành
Map<String, Double> avgByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.averagingInt(Student::grade)
    ));
// {CS=87.67, Math=85.0}

// Lấy tên sinh viên mỗi ngành (thay vì toàn bộ object)
Map<String, List<String>> namesByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.mapping(Student::name, Collectors.toList())
    ));
// {CS=["An", "Bình", "Em"], Math=["Cường", "Dũng"]}

// Điểm cao nhất mỗi ngành
Map<String, Optional<Student>> topByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.maxBy(Comparator.comparingInt(Student::grade))
    ));
// {CS=Optional[Bình(90)], Math=Optional[Dũng(92)]}
```

#### groupingBy lồng nhau (nested grouping)

```java
record Employee(String name, String dept, String level) {}

List<Employee> employees = List.of(
    new Employee("An", "Engineering", "Senior"),
    new Employee("Bình", "Engineering", "Junior"),
    new Employee("Cường", "Engineering", "Senior"),
    new Employee("Dũng", "Marketing", "Senior"),
    new Employee("Em", "Marketing", "Junior")
);

// Nhóm theo dept → rồi nhóm tiếp theo level
Map<String, Map<String, List<Employee>>> nested = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::dept,
        Collectors.groupingBy(Employee::level)
    ));
// {
//   Engineering → {
//     Senior → [An, Cường],
//     Junior → [Bình]
//   },
//   Marketing → {
//     Senior → [Dũng],
//     Junior → [Em]
//   }
// }

{
     Nam: { 
         Toan: 40xStudent
         Van: 40xStudent 
     Nu: {
          Toan
          Van
}
```



**So sánh:**

```
┌──────────────────────────────────────────────────────────────────────────┐
│ Java:   stream.collect(groupingBy(Student::major))                       │
│                                                                          │
│ JS/TS:  Object.groupBy(arr, s => s.major)          (ES2024)              │
│         // hoặc dùng reduce:                                             │
│         arr.reduce((acc, s) => {                                         │
│           (acc[s.major] ??= []).push(s); return acc;                     │
│         }, {})                                                           │
│                                                                          │
│ Python: from itertools import groupby                                    │
│         {k: list(v) for k, v in groupby(sorted(lst, key=f), key=f)}     │
│         // hoặc dùng defaultdict:                                        │
│         d = defaultdict(list)                                            │
│         for s in students: d[s.major].append(s)                          │
└──────────────────────────────────────────────────────────────────────────┘
```

### 4.4 partitioningBy() — Chia Làm Hai Nhóm

Chia stream thành đúng 2 nhóm: `true` và `false` dựa trên predicate. Đặc biệt hữu ích khi cần chia dữ liệu thành "đạt/không đạt", "chẵn/lẻ", v.v.

```java
Map<Boolean, List<Student>> passOrFail = students.stream()
    .collect(Collectors.partitioningBy(s -> s.grade() >= 80));
// {
//   true  → [An(85), Bình(90), Em(88), Dũng(92)],
//   false → [Cường(78)]
// }

// partitioningBy cũng hỗ trợ downstream collector
Map<Boolean, Long> countPassFail = students.stream()
    .collect(Collectors.partitioningBy(
        s -> s.grade() >= 80,
        Collectors.counting()
    ));
// {true=4, false=1}
```

**partitioningBy vs groupingBy:**


|           | `partitioningBy`         | `groupingBy`           |
| --------- | ------------------------ | ---------------------- |
| Số nhóm   | Luôn 2 (true/false)      | Không giới hạn         |
| Key type  | `Boolean`                | Bất kỳ                 |
| Nhóm rỗng | Luôn có cả true và false | Chỉ có nhóm có phần tử |


### 4.5 joining() — Nối Chuỗi

```java
List<String> names = List.of("An", "Bình", "Cường");

// Nối đơn giản
String joined1 = names.stream()
    .collect(Collectors.joining());
// "AnBìnhCường"

// Nối với delimiter
String joined2 = names.stream()
    .collect(Collectors.joining(", "));
// "An, Bình, Cường"

// Nối với delimiter, prefix, suffix
String joined3 = names.stream()
    .collect(Collectors.joining(", ", "[", "]"));
// "[An, Bình, Cường]"
```

**So sánh:**

```
┌──────────────────────────────────────────────────────────────────┐
│ Java:   stream.collect(Collectors.joining(", "))                 │
│ JS/TS:  arr.join(", ")                                           │
│ Python: ", ".join(lst)                                           │
└──────────────────────────────────────────────────────────────────┘
```

### 4.6 summarizingInt/Double/Long — Thống Kê Nhanh

Thu thập đồng thời count, sum, min, average, max.

```java
IntSummaryStatistics stats = students.stream()
    .collect(Collectors.summarizingInt(Student::grade));

System.out.println("Số SV:      " + stats.getCount());    // 5
System.out.println("Tổng điểm:  " + stats.getSum());      // 433
System.out.println("Điểm TB:    " + stats.getAverage());  // 86.6
System.out.println("Điểm thấp:  " + stats.getMin());      // 78
System.out.println("Điểm cao:   " + stats.getMax());      // 92
```

### 4.7 counting(), mapping(), reducing() — Downstream Collectors

Các collector này thường dùng **bên trong** `groupingBy` hoặc `partitioningBy` như downstream collector.

```java
// counting() — đếm phần tử trong mỗi nhóm
Map<String, Long> countByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.counting()
    ));

// mapping() — biến đổi phần tử trước khi thu thập
Map<String, Set<String>> nameSetByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.mapping(Student::name, Collectors.toSet())
    ));

// reducing() — gộp phần tử trong mỗi nhóm
Map<String, Integer> totalGradeByMajor = students.stream()
    .collect(Collectors.groupingBy(
        Student::major,
        Collectors.reducing(0, Student::grade, Integer::sum)
    ));
// {CS=263, Math=170}
```

### 4.8 Bảng Tổng Hợp Collectors


| Collector                            | Kết quả                 | Ví dụ                                          |
| ------------------------------------ | ----------------------- | ---------------------------------------------- |
| `toList()`                           | `List<T>`               | `.collect(toList())`                           |
| `toSet()`                            | `Set<T>`                | `.collect(toSet())`                            |
| `toUnmodifiableList()`               | Immutable `List<T>`     | `.collect(toUnmodifiableList())`               |
| `toMap(keyFn, valFn)`                | `Map<K,V>`              | `.collect(toMap(User::name, User::age))`       |
| `groupingBy(classifier)`             | `Map<K, List<T>>`       | `.collect(groupingBy(User::dept))`             |
| `groupingBy(classifier, downstream)` | `Map<K, D>`             | `.collect(groupingBy(User::dept, counting()))` |
| `partitioningBy(predicate)`          | `Map<Boolean, List<T>>` | `.collect(partitioningBy(u -> u.age > 18))`    |
| `joining(delimiter)`                 | `String`                | `.collect(joining(", "))`                      |
| `summarizingInt(fn)`                 | `IntSummaryStatistics`  | `.collect(summarizingInt(User::age))`          |
| `counting()`                         | `Long`                  | Dùng làm downstream                            |
| `mapping(fn, downstream)`            | Tuỳ downstream          | Dùng làm downstream                            |
| `reducing(identity, fn, op)`         | `T`                     | Dùng làm downstream                            |


---

## 5. reduce() Deep Dive

`reduce()` gộp (fold) tất cả phần tử của stream thành **một giá trị duy nhất**. Đây là phép toán nền tảng — nhiều terminal operations khác (count, sum, min, max) đều có thể triển khai bằng reduce.

### 5.1 Ba Dạng reduce()

#### Dạng 1: reduce(BinaryOperator) — không có identity

Trả về `Optional<T>` vì stream có thể rỗng.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

Optional<Integer> sum = numbers.stream()
    .reduce((a, b) -> a + b);
// Optional[15]

Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);
// Optional[5]

// Stream rỗng
Optional<Integer> empty = List.<Integer>of().stream()
    .reduce(Integer::sum);
// Optional.empty
```

#### Dạng 2: reduce(identity, BinaryOperator) — có identity

Trả về `T` (không cần Optional vì có giá trị mặc định).

```java
// identity = giá trị khởi tạo, cũng là giá trị trả về khi stream rỗng
int sum = List.of(1, 2, 3, 4, 5).stream()
    .reduce(0, Integer::sum);
// 15

// Cách hoạt động bên trong:
// Bước 0: result = 0          (identity)
// Bước 1: result = 0 + 1 = 1
// Bước 2: result = 1 + 2 = 3
// Bước 3: result = 3 + 3 = 6
// Bước 4: result = 6 + 4 = 10
// Bước 5: result = 10 + 5 = 15

// Nối chuỗi
String concat = List.of("Hello", " ", "World").stream()
    .reduce("", String::concat);
// "Hello World"

// Stream rỗng → trả về identity
int emptySum = List.<Integer>of().stream()
    .reduce(0, Integer::sum);
// 0
```

#### Dạng 3: reduce(identity, accumulator, combiner) — cho parallel streams

```java
// combiner dùng để gộp kết quả từ các thread khác nhau trong parallel stream
int sum = List.of(1, 2, 3, 4, 5).parallelStream()
    .reduce(
        0,                // identity
        Integer::sum,     // accumulator: gộp phần tử vào kết quả
        Integer::sum      // combiner: gộp hai kết quả partial
    );

// Ví dụ: tính tổng độ dài chuỗi (kiểu đầu vào khác kiểu kết quả)
int totalLength = List.of("An", "Bình", "Cường").stream()
    .reduce(
        0,                           // identity (int)
        (len, str) -> len + str.length(),  // accumulator: int + String → int
        Integer::sum                 // combiner: int + int → int
    );
// 12  (2 + 4 + 6)
```

### 5.2 So Sánh reduce() Giữa Ba Ngôn Ngữ

```
┌───────────────────────────────────────────────────────────────────────┐
│ JAVA:                                                                 │
│   stream.reduce(0, (acc, x) -> acc + x)                               │
│   stream.reduce(0, Integer::sum)              // method reference     │
│                                                                       │
│ JAVASCRIPT:                                                           │
│   arr.reduce((acc, x) => acc + x, 0)         // ← init value ở CUỐI  │
│                                                                       │
│ PYTHON:                                                               │
│   from functools import reduce                                        │
│   reduce(lambda acc, x: acc + x, lst, 0)     // ← init value ở CUỐI  │
│                                                                       │
│ LƯU Ý: Java đặt identity ở ĐẦU, JS/Python đặt ở CUỐI!              │
└───────────────────────────────────────────────────────────────────────┘
```

### 5.3 Quy Tắc Chọn Identity

Identity phải thoả mãn: `accumulator(identity, x) == x` cho mọi x.


| Phép toán | Identity            | Giải thích        |
| --------- | ------------------- | ----------------- |
| Cộng      | `0`                 | `0 + x = x`       |
| Nhân      | `1`                 | `1 * x = x`       |
| Nối chuỗi | `""`                | `"" + s = s`      |
| Max (int) | `Integer.MIN_VALUE` | `max(MIN, x) = x` |
| Min (int) | `Integer.MAX_VALUE` | `min(MAX, x) = x` |
| AND logic | `true`              | `true && x = x`   |
| OR logic  | `false`             | `false            |


### 5.4 Khi Nào Dùng reduce() vs Collectors

```java
// Dùng reduce — khi gộp thành MỘT giá trị đơn
int total = orders.stream()
    .map(Order::getAmount)
    .reduce(0, Integer::sum);

// Dùng Collectors — khi thu thập thành COLLECTION hoặc cấu trúc phức tạp
Map<String, List<Order>> byCategory = orders.stream()
    .collect(Collectors.groupingBy(Order::getCategory));

// TIP: Cho tính tổng/trung bình, Java có sẵn:
int sum = numbers.stream().mapToInt(Integer::intValue).sum();
double avg = numbers.stream().mapToInt(Integer::intValue).average().orElse(0);
```

---

## 6. flatMap Explained

### 6.1 Vấn Đề: Stream Lồng Nhau

Khi bạn `map()` và kết quả là một collection, bạn sẽ có `Stream<List<T>>` thay vì `Stream<T>`:

```java
record Order(String customer, List<String> items) {}

List<Order> orders = List.of(
    new Order("An", List.of("Laptop", "Mouse")),
    new Order("Bình", List.of("Keyboard", "Monitor", "USB"))
);

// Vấn đề: muốn lấy TẤT CẢ items → nhưng map cho ra Stream<List<String>>
Stream<List<String>> nested = orders.stream()
    .map(Order::items);
// Stream chứa: [["Laptop","Mouse"], ["Keyboard","Monitor","USB"]] => nested list (2D)
// → Chúng ta muốn: ["Laptop","Mouse","Keyboard","Monitor","USB"] => list 1 Dimension (1D) 
```

### 6.2 Giải Pháp: flatMap

`flatMap` = `map` + `flatten`. Nó biến đổi mỗi phần tử thành một Stream, rồi **nối tất cả các stream con lại** thành một stream duy nhất.

```java
List<String> allItems = orders.stream()
    .flatMap(order -> order.items().stream())  // List<String> → Stream<String>
    .collect(Collectors.toList());
// ["Laptop", "Mouse", "Keyboard", "Monitor", "USB"] => list 1D
```

ASCII diagram:

```
  orders.stream()
  ┌──────────────────────┐   ┌──────────────────────────────┐
  │ Order("An",          │   │ Order("Bình",                │
  │   ["Laptop","Mouse"])│   │   ["Keyboard","Monitor","USB"])│
  └──────────┬───────────┘   └──────────────┬───────────────┘
             │                               │
             ▼  .flatMap(o -> o.items().stream())
  ┌──────────────────┐       ┌──────────────────────────┐
  │ Stream("Laptop", │       │ Stream("Keyboard",       │
  │        "Mouse")  │       │        "Monitor", "USB") │
  └────────┬─────────┘       └────────────┬─────────────┘
           │                              │
           └──────────┬───────────────────┘
                      ▼  flatten (nối thành 1 stream)
  ┌─────────────────────────────────────────────────────┐
  │ Stream("Laptop", "Mouse", "Keyboard", "Monitor",   │
  │        "USB")                                       │
  └─────────────────────────────────────────────────────┘
```

### 6.3 Thêm Ví Dụ Thực Tế

#### Tách từ trong danh sách câu

```java
List<String> sentences = List.of(
    "Hello World",
    "Java Streams are powerful",
    "flatMap is useful"
);

List<String> words = sentences.stream()
    .flatMap(sentence -> Arrays.stream(sentence.split(" "))) // => .map: [["Hello", "World"], ["Java",..], ["flatMap", ...] ]
    .collect(Collectors.toList());
// ["Hello", "World", "Java", "Streams", "are", "powerful", "flatMap", "is", "useful"]
```

#### flatMap với Optional (Java 9+)

```java
// Lọc bỏ Optional.empty
List<Optional<String>> optionals = List.of(
    Optional.of("An"),
    Optional.empty(),
    Optional.of("Bình"),
    Optional.empty()
);

List<String> present = optionals.stream()
    .flatMap(Optional::stream)   // Optional.stream() trả về Stream 0 hoặc 1 phần tử
    .collect(Collectors.toList());
// ["An", "Bình"]
```

#### Tạo tất cả cặp (pairs) từ hai list

```java
List<String> colors = List.of("Red", "Blue");
List<String> sizes = List.of("S", "M", "L");

List<String> combinations = colors.stream()
    .flatMap(color -> sizes.stream()
        .map(size -> color + "-" + size))
    .collect(Collectors.toList());
// ["Red-S", "Red-M", "Red-L", "Blue-S", "Blue-M", "Blue-L"]
```

### 6.4 So Sánh flatMap

```
┌──────────────────────────────────────────────────────────────────────────┐
│ JAVA:                                                                    │
│   stream.flatMap(list -> list.stream())                                  │
│   stream.flatMap(Collection::stream)                                     │
│                                                                          │
│ JAVASCRIPT:                                                              │
│   arr.flatMap(x => x)           // flatten 1 level                       │
│   arr.flat()                    // flatten 1 level                       │
│   arr.flat(Infinity)            // flatten tất cả levels                 │
│                                                                          │
│ PYTHON:                                                                  │
│   [item for sublist in nested for item in sublist]                       │
│   // hoặc:                                                               │
│   from itertools import chain                                            │
│   list(chain.from_iterable(nested))                                      │
│                                                                          │
│ LƯU Ý: Java flatMap CHỈ flatten 1 level (giống JS .flat(1))             │
└──────────────────────────────────────────────────────────────────────────┘
```

---

## 7. Parallel Streams

### 7.1 Cách Sử Dụng

```java
// Cách 1: Từ collection
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
int sum = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();

// Cách 2: Chuyển từ sequential sang parallel
int sum2 = numbers.stream()
    .parallel()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
```

### 7.2 Cách Hoạt Động — Fork/Join Framework

```
  parallelStream() sử dụng ForkJoinPool.commonPool()

  ┌─────────────────────────────────────────────────────┐
  │              ForkJoinPool (common pool)              │
  │                                                     │
  │  Dữ liệu gốc: [1, 2, 3, 4, 5, 6, 7, 8]           │
  │                         │                           │
  │                      FORK (chia)                    │
  │                    ┌────┴────┐                      │
  │              [1,2,3,4]    [5,6,7,8]                 │
  │              ┌───┴───┐    ┌───┴───┐                 │
  │           [1,2]   [3,4] [5,6]   [7,8]              │
  │             │       │     │       │                 │
  │           Thread1 Thread2 Thread3 Thread4           │
  │          (sum=3) (sum=7) (sum=11) (sum=15)          │
  │              └───┬───┘    └───┬───┘                 │
  │                JOIN (gộp)  JOIN                     │
  │               (sum=10)   (sum=26)                   │
  │                   └───┬───┘                         │
  │                     JOIN                            │
  │                   (sum=36)                          │
  └─────────────────────────────────────────────────────┘
```

### 7.3 Khi Nào NÊN Dùng Parallel Streams


| Tiêu chí              | Nên dùng parallel                  | Không nên dùng parallel          |
| --------------------- | ---------------------------------- | -------------------------------- |
| Kích thước dữ liệu    | > 10,000 phần tử                   | < 1,000 phần tử                  |
| Kiểu operation        | Stateless (filter, map)            | Stateful (sorted, distinct)      |
| Source                | ArrayList, arrays, IntStream.range | LinkedList, Stream.iterate       |
| Tính toán mỗi phần tử | Nặng (CPU-intensive)               | Nhẹ (simple comparison)          |
| Thread safety         | Operations không có side-effects   | Ghi vào shared state             |
| Môi trường            | Standalone application             | Web server (đã dùng thread pool) |


### 7.4 Khi Nào KHÔNG NÊN Dùng

#### Lý do 1: Overhead lớn hơn benefit

```java
// ĐỪNG LÀM THẾ NÀY — dữ liệu quá nhỏ, overhead của parallelism > benefit
List<Integer> small = List.of(1, 2, 3, 4, 5);
int sum = small.parallelStream()   // CHẬM HƠN sequential!
    .reduce(0, Integer::sum);
```

#### Lý do 2: Source không phù hợp

```java
// LinkedList — chia (split) rất chậm vì phải duyệt tuần tự
LinkedList<Integer> linked = new LinkedList<>(List.of(1, 2, 3, 4, 5));
linked.parallelStream()  // Hiệu suất kém vì LinkedList khó chia đều
    .map(x -> x * 2)
    .toList();

// ArrayList, arrays — chia nhanh vì truy cập random
ArrayList<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
arrayList.parallelStream()  // Tốt hơn nhiều
    .map(x -> x * 2)
    .toList();
```

#### Lý do 3: Không thread-safe — Side effects

```java
// SAI! — Race condition: nhiều thread ghi vào cùng một list
List<Integer> results = new ArrayList<>();
numbers.parallelStream()
    .filter(n -> n > 3)
    .forEach(results::add);   // BUG! ArrayList không thread-safe
// Kết quả không đoán trước, có thể mất phần tử hoặc ném exception

// ĐÚNG — dùng collect thay vì forEach + shared state
List<Integer> results = numbers.parallelStream()
    .filter(n -> n > 3)
    .collect(Collectors.toList());   // Thread-safe
```

#### Lý do 4: Thứ tự quan trọng

```java
// forEachOrdered giữ thứ tự nhưng CHẬM (mất lợi thế parallel)
numbers.parallelStream()
    .forEachOrdered(System.out::println);  // giữ thứ tự nhưng chậm

// forEach không đảm bảo thứ tự
numbers.parallelStream()
    .forEach(System.out::println);  // thứ tự ngẫu nhiên
```

#### Lý do 5: Web server đã dùng thread pool

```
Trong Spring Boot / web server, mỗi request đã chạy trên 1 thread.
parallelStream() dùng chung ForkJoinPool.commonPool() với TẤT CẢ requests.

→ Nếu 100 requests đồng thời, tất cả tranh giành commonPool (thường chỉ có
  Runtime.getRuntime().availableProcessors() - 1 threads)

→ Có thể gây BOTTLENECK cho toàn bộ application!

Giải pháp: Dùng sequential stream, hoặc tạo ForkJoinPool riêng:

ForkJoinPool customPool = new ForkJoinPool(4);
customPool.submit(() -> {
    list.parallelStream()
        .map(x -> heavyComputation(x))
        .collect(Collectors.toList());
}).get();
customPool.shutdown();
```

### 7.5 Checklist Trước Khi Dùng Parallel Stream

```
□ Dữ liệu có > 10,000 phần tử không?
□ Tính toán mỗi phần tử có nặng (CPU-intensive) không?
□ Source có phải ArrayList/array/IntStream.range không?
□ Operations có stateless (filter, map) không?
□ Không có side-effects (ghi vào shared state)?
□ Không chạy trong web server context (hoặc dùng custom pool)?
□ Đã benchmark và parallel thực sự nhanh hơn?

→ Nếu KHÔNG đạt đủ các điều kiện trên: dùng sequential stream.
```

---

## 8. Common Patterns — Các Mẫu Thường Gặp

### 8.1 Frequency Counting — Đếm Tần Suất

```java
List<String> words = List.of("java", "python", "java", "go", "python", "java");

// Đếm tần suất mỗi từ
Map<String, Long> frequency = words.stream()
    .collect(Collectors.groupingBy(
        Function.identity(),    // key = chính phần tử đó
        Collectors.counting()
    ));
// {java=3, python=2, go=1}

// Tìm từ xuất hiện nhiều nhất
String mostCommon = frequency.entrySet().stream()
    .max(Map.Entry.comparingByValue())
    .map(Map.Entry::getKey)
    .orElse("N/A");
// "java"
```

**So sánh:**

```
┌────────────────────────────────────────────────────────────────────────┐
│ Java:   stream.collect(groupingBy(identity(), counting()))             │
│                                                                        │
│ JS/TS:  arr.reduce((acc, w) => {                                       │
│           acc[w] = (acc[w] || 0) + 1; return acc;                      │
│         }, {})                                                         │
│                                                                        │
│ Python: from collections import Counter                                │
│         Counter(words)                 ← Python thắng về ngắn gọn!     │
└────────────────────────────────────────────────────────────────────────┘
```

### 8.2 Partitioning Data — Chia Dữ Liệu

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Chia thành chẵn/lẻ
Map<Boolean, List<Integer>> evenOdd = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// {true=[2,4,6,8,10], false=[1,3,5,7,9]}

List<Integer> evens = evenOdd.get(true);
List<Integer> odds = evenOdd.get(false);
```

### 8.3 Finding Max/Min với Comparator

```java
record Product(String name, double price, String category) {}

List<Product> products = List.of(
    new Product("Laptop", 1200.0, "Electronics"),
    new Product("Phone", 800.0, "Electronics"),
    new Product("Book", 15.0, "Education"),
    new Product("Course", 50.0, "Education")
);

// Sản phẩm đắt nhất
Optional<Product> mostExpensive = products.stream()
    .max(Comparator.comparingDouble(Product::price));
// Optional[Product[name=Laptop, price=1200.0, category=Electronics]]

// Sản phẩm rẻ nhất trong mỗi category
Map<String, Optional<Product>> cheapestByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::category,
        Collectors.minBy(Comparator.comparingDouble(Product::price))
    ));
// {Electronics=Optional[Phone(800)], Education=Optional[Book(15)]}

// Top 3 sản phẩm đắt nhất
List<Product> top3 = products.stream()
    .sorted(Comparator.comparingDouble(Product::price).reversed())
    .limit(3)
    .collect(Collectors.toList());
```

### 8.4 Converting Between Collections

```java
// List → Set (loại bỏ trùng lặp)
List<String> list = List.of("a", "b", "a", "c");
Set<String> set = new HashSet<>(list);                    // không cần stream
Set<String> set2 = list.stream().collect(Collectors.toSet()); // dùng stream nếu cần filter/map

// Set → Sorted List
Set<Integer> numberSet = Set.of(3, 1, 4, 1, 5);
List<Integer> sortedList = numberSet.stream()
    .sorted()
    .collect(Collectors.toList());

// List<T> → Map<K, T>
Map<String, Product> productByName = products.stream()
    .collect(Collectors.toMap(Product::name, Function.identity()));

// Map → List of entries sorted by value
Map<String, Integer> scores = Map.of("An", 85, "Bình", 92, "Cường", 78);
List<Map.Entry<String, Integer>> sorted = scores.entrySet().stream()
    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    .collect(Collectors.toList());
// [Bình=92, An=85, Cường=78]

// Array → Stream → List
String[] arr = {"a", "b", "c"};
List<String> fromArray = Arrays.stream(arr)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// int[] → List<Integer>  (chú ý: cần boxed())
int[] intArr = {1, 2, 3, 4, 5};
List<Integer> boxed = Arrays.stream(intArr)
    .boxed()
    .collect(Collectors.toList());
```

### 8.5 Tổng Hợp So Sánh Java Streams vs Python vs JavaScript


| Thao tác          | Java Streams                         | Python                          | JavaScript/TypeScript               |
| ----------------- | ------------------------------------ | ------------------------------- | ----------------------------------- |
| Filter            | `.filter(x -> x > 5)`                | `[x for x in l if x > 5]`       | `.filter(x => x > 5)`               |
| Map               | `.map(x -> x * 2)`                   | `[x * 2 for x in l]`            | `.map(x => x * 2)`                  |
| FlatMap           | `.flatMap(Collection::stream)`       | `[i for sub in l for i in sub]` | `.flatMap(x => x)`                  |
| Sort              | `.sorted(comp)`                      | `sorted(l, key=f)`              | `.sort((a,b) => ...)`               |
| Distinct          | `.distinct()`                        | `list(set(l))`                  | `[...new Set(a)]`                   |
| Take N            | `.limit(n)`                          | `l[:n]`                         | `.slice(0, n)`                      |
| Skip N            | `.skip(n)`                           | `l[n:]`                         | `.slice(n)`                         |
| Reduce            | `.reduce(init, fn)`                  | `reduce(fn, l, init)`           | `.reduce(fn, init)`                 |
| Group by          | `groupingBy(fn)`                     | `groupby(sorted(l))`            | `Object.groupBy(a, fn)`             |
| Count             | `.count()`                           | `len(l)`                        | `.length`                           |
| Find first        | `.findFirst()` → `Optional`          | `next(gen, None)`               | `.find(fn)`                         |
| Any match         | `.anyMatch(fn)`                      | `any(gen)`                      | `.some(fn)`                         |
| All match         | `.allMatch(fn)`                      | `all(gen)`                      | `.every(fn)`                        |
| Join strings      | `joining(", ")`                      | `", ".join(l)`                  | `.join(", ")`                       |
| Min/Max           | `.min(comp)` / `.max(comp)`          | `min(l)` / `max(l)`             | `Math.min(...a)` / `Math.max(...a)` |
| Frequency         | `groupingBy(identity(), counting())` | `Counter(l)`                    | `reduce` + object                   |
| Lazy?             | Co (lazy)                            | List comp: khong, Generator: co | Khong (eager)                       |
| Immutable source? | Co                                   | Co (tao list moi)               | `.sort()` mutates!                  |
| Reusable?         | Khong (1 lan)                        | Generator: khong, List: co      | Co                                  |


---

## Tóm Tắt

### Bảng Tóm Tắt Stream API


| Khái niệm            | Điểm chính                                              | Ghi nhớ                                                    |
| -------------------- | ------------------------------------------------------- | ---------------------------------------------------------- |
| **Stream Pipeline**  | Source → Intermediate ops → Terminal op                 | Stream là "kế hoạch thực thi", không phải cấu trúc dữ liệu |
| **Lazy evaluation**  | Intermediate ops không chạy cho đến khi gặp terminal op | Khác JS array methods (eager). Giống Python generators     |
| **Single use**       | Một stream chỉ dùng được một lần                        | Gọi terminal op lần 2 → `IllegalStateException`            |
| **Immutable source** | Stream không thay đổi collection gốc                    | An toàn — source collection vẫn nguyên vẹn                 |
| **filter**           | Lọc phần tử theo Predicate                              | `stream.filter(x -> x > 0)`                                |
| **map**              | Biến đổi T → R                                          | `stream.map(String::toUpperCase)`                          |
| **flatMap**          | Phẳng hoá stream lồng nhau                              | `Stream<List<T>>` → `Stream<T>`                            |
| **sorted**           | Sắp xếp (stateful)                                      | Không mutate source (khác JS `.sort()`)                    |
| **distinct**         | Loại bỏ trùng lặp (dựa trên `.equals()`)                | Tương đương `[...new Set(arr)]` trong JS                   |
| **limit / skip**     | Lấy/bỏ N phần tử đầu                                    | Phân trang: `.skip((page-1)*size).limit(size)`             |
| **collect**          | Thu thập kết quả vào collection                         | `Collectors.toList()`, `toSet()`, `toMap()`                |
| **toList()**         | Java 16+ shorthand                                      | Trả về **unmodifiable** list                               |
| **reduce**           | Gộp thành một giá trị                                   | Identity ở ĐẦU (khác JS/Python ở cuối)                     |
| **groupingBy**       | Nhóm phần tử (như SQL GROUP BY)                         | Hỗ trợ downstream: `groupingBy(fn, counting())`            |
| **partitioningBy**   | Chia 2 nhóm true/false                                  | Luôn có cả 2 key, kể cả nhóm rỗng                          |
| **joining**          | Nối chuỗi                                               | `joining(", ", "[", "]")` — delimiter, prefix, suffix      |
| **Parallel streams** | Chia dữ liệu cho nhiều thread (Fork/Join)               | Chỉ dùng khi: data lớn, CPU-intensive, stateless ops       |
| **Thread safety**    | Parallel stream + shared mutable state = BUG            | Dùng `collect()` thay vì `forEach` + shared list           |


### Quy Tắc Vàng

```
1. Ưu tiên SEQUENTIAL stream — parallel chỉ khi đã benchmark
2. Tránh side-effects trong stream operations
3. Dùng method references khi có thể: User::getName thay vì u -> u.getName()
4. Dùng primitive streams (IntStream, LongStream) cho số để tránh boxing
5. Stream dùng 1 lần — nếu cần dùng lại, tạo stream mới từ source
6. collect() cho collection, reduce() cho single value
7. groupingBy + downstream collector = công cụ mạnh nhất của Collectors
```

