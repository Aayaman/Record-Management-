# 📚 Record-Management-

##  Book Records Processing System

A Java project to clean, validate, categorize, serialize, and interactively browse book records from CSV files. Built in **three parts**, this tool helps transform messy book data into structured, navigable collections. 🛠️📦

---

## 🧩 Project Parts

### 📝 Part 1: Syntax Check & Categorization
- Reads CSV files listed in `input_file_names.txt`.
- Detects syntax issues: too many/few/missing fields, unknown genres.
- ✅ Valid records → genre-based `.csv` files  
- ❌ Invalid records → `syntax_error_file.txt`

### 🧪 Part 2: Semantic Validation & Serialization
- Reads categorized CSV files.
- Validates:
  - ISBN-10 / ISBN-13 📕
  - Price 💰
  - Year 📆
- ✅ Valid → serialized `.ser` files  
- ❌ Invalid → `semantic_error_file.txt`

### 🔍 Part 3: Interactive Viewer
- Deserializes `.ser` files into arrays of `Book` objects.
- Menu lets you:
  - 🔄 Navigate books (+/- n)
  - 📂 Select file to view
  - ❌ Exit viewer
- Shows `BOF`/`EOF` when limits are reached.
