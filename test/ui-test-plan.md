# UI test plan

## Test configuration

- **Java version:** Java 25
- **Build command:** `javac -d out (Get-ChildItem -Path src/main/java -Filter *.java -Recurse).FullName`
- **Run command:** `java -cp out sandy.Sandy`
- **Comparison rule:** Exact output after line-ending normalization only.
- **Test isolation:** Remove `data/sandy.txt` before each case, then apply any case-specific data-file setup.

## Test cases

### Add, list, mark, and unmark a task

**Aim:** Verify that a task can be added, displayed, marked complete, and marked incomplete again within one UI session.

**Inputs:**

```text
list
todo plan presentation
list
mark 1
list
unmark 1
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] plan presentation
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Create and display all task types

**Aim:** Verify that todos, deadlines, and events are stored as `Task` objects, retain date/time text as entered, and display their type and completion status correctly.

**Inputs:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by no idea :-p
mark 2
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][X] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Expected data file:**

```text
T | 0 | borrow book
D | 1 | return book | Sunday
E | 0 | project meeting | Mon 2pm | 4pm
D | 0 | do homework | no idea :-p
```

### Load tasks from an existing data file

**Aim:** Verify that Sandy loads all task types and their completion statuses when it starts.

**Data-file setup:**

```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle invalid commands and continue

**Aim:** Verify that invalid commands show useful errors without stopping the command loop.

**Inputs:**

```text
todo
blah
list
mark abc
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 Oops! I do not recognize that command.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle a corrupted data file

**Aim:** Verify that Sandy reports corrupted saved data, starts with an empty list, and remains usable.

**Data-file setup:**

```text
invalid saved task
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! I could not load saved tasks: The data file is corrupted at line 1.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Latest test session

- **Java version:** 25.0.4
- **Last run:** 2026-09-15
- **Result:** Passed - 5 of 5 test cases.

### Add, list, mark, and unmark a task

**Console input:**

```text
list
todo plan presentation
list
mark 1
list
unmark 1
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] plan presentation
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [T][ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] plan presentation
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Create and display all task types

**Console input:**

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
deadline do homework /by no idea :-p
mark 2
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 4 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][X] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
 4.[D][ ] do homework (by: no idea :-p)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

**Data file after run:**

```text
T | 0 | borrow book
D | 1 | return book | Sunday
E | 0 | project meeting | Mon 2pm | 4pm
D | 0 | do homework | no idea :-p
```

### Load tasks from an existing data file

**Data-file setup:**

```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

**Console input:**

```text
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle invalid commands and continue

**Console input:**

```text
todo
blah
list
mark abc
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 Oops! I do not recognize that command.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Oops! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Handle a corrupted data file

**Data-file setup:**

```text
invalid saved task
```

**Console input:**

```text
list
bye
```

**Console output:**

```text
____________________________________________________________
 ____                  _       
/ ___|  __ _ _ __   __| |_   _ 
\___ \ / _` | '_ \ / _` | | | |
 ___) | (_| | | | | (_| | |_| |
|____/ \__,_|_| |_|\__,_|\__, |
                         |___/ 
Hello! I'm Sandy.
What can I do for you?
____________________________________________________________
____________________________________________________________
 Oops! I could not load saved tasks: The data file is corrupted at line 1.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
