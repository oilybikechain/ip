# UI test plan

## Test configuration

- **Java version:** Java 25
- **Build command:** `javac -d out src/main/java/sandy/*.java`
- **Run command:** `java -cp out sandy.Sandy`
- **Comparison rule:** Exact output after line-ending normalization only.

## Test cases

### Add, list, mark, and unmark a task

**Aim:** Verify that a task can be added, displayed, marked complete, and marked incomplete again within one UI session.

**Inputs:**

```text
list
plan presentation
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
 added: plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] plan presentation
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

## Latest test session

- **Java version:** 25.0.4
- **Last run:** 2026-08-31 (session 2)
- **Result:** Passed — 2 of 2 test cases.

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

### Add, list, mark, and unmark a task

**Console input:**

```text
list
plan presentation
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
 added: plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] plan presentation
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [X] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[X] plan presentation
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [ ] plan presentation
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] plan presentation
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
