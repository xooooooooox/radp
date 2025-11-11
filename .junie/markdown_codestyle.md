## 1) Golden Rules for Markdown

1. **Always fence code** with triple backticks and a **language tag**. _Never_ use bare text or 4‑space indents for
   code/config.
2. **Use inline code** (single backticks) for identifiers, CLI flags, file names, env vars.
3. One H1 (`#`) per document. Subsequent sections start at `##`.
4. Keep lines ≤ 120 chars; hard wrap paragraphs; do **not** hard wrap code.
5. Use ASCII punctuation in code blocks; prefer UTF‑8 in prose only.
6. Prefer lists over long paragraphs; keep list markers consistent (`-`).
7. When showing **paths or commands**, include a leading working dir hint and prompt (e.g., `$`, `PS>`).

---

## 2) Code Block Policy (Strict)

**Junie must follow these rules when outputting code or config:**

- Fenced code block start: ```` ```<language> ````  
  Fenced code block end: ```` ``` ````
- Do **not** include surrounding quotes or Markdown indentation around blocks.
- Do **not** mix prose and code in the same block. Each block contains only code.
- Provide **minimal runnable context** (imports/class/`main` as needed) unless the snippet is clearly partial.
- For multi‑file samples, label each with a filename line above the block using bold text, e.g. `**pom.xml**`.

### Language tag mapping

Use the most specific tag from this table:

| Content type            | Language tag |
|-------------------------|--------------|
| Java source             | `java`       |
| Maven POM or XML config | `xml`        |
| YAML (Spring)           | `yaml`       |
| Properties              | `properties` |
| Shell/CLI (Linux/Mac)   | `bash`       |
| PowerShell (Windows)    | `powershell` |
| JSON                    | `json`       |
| SQL                     | `sql`        |
| Markdown example        | `markdown`   |
| Log output              | `text`       |
