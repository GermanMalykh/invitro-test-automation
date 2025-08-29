<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Automation Session — ${automation_session.status!''?cap_first}</title>
  <link href="https://yastatic.net/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
  <style>
    :root{
      --bg:#f6f8fb; --text:#121826; --muted:#5b6475; --card:#ffffff; --border:#e5e9f0;
      --shadow:0 6px 18px rgba(16,24,40,.06); --accent:#2563eb;
      --ok:#16a34a; --warn:#d97706; --err:#dc2626;
    }
    html,body{background:var(--bg);color:var(--text);} body{padding:28px}
    .container-narrow{max-width:1080px;margin:0 auto}
    .stack{display:flex;flex-direction:column;gap:18px}
    .page-title{margin:0 0 16px;font-weight:800}

    /* чип по умолчанию читаемый, даже если статус пустой */
    .chip{display:inline-block;padding:4px 10px;border-radius:999px;font-size:12px;line-height:1;
          border:1px solid var(--border); background:#eef2f7; color:#334155; font-weight:600}
    .chip.done,.chip.passed{background:var(--ok);border-color:var(--ok);color:#fff}
    .chip.failed,.chip.error{background:var(--err);border-color:var(--err);color:#fff}
    .chip.running,.chip.queued,.chip.warning,.chip.timeout{background:var(--warn);border-color:var(--warn);color:#fff}

    .card{background:var(--card);border:1px solid var(--border);border-radius:14px;box-shadow:var(--shadow);overflow:hidden}
    .card-header{padding:14px 16px;border-bottom:1px solid var(--border);display:flex;align-items:center;gap:10px}
    .card-title{font-size:16px;font-weight:700;margin:0}
    .card-body{padding:16px}

    .kv{display:grid;grid-template-columns:200px 1fr;gap:10px 16px}
    .kv .k{color:var(--muted)}
    .mono{font-family:Menlo,Monaco,Consolas,"Liberation Mono",monospace}
    .break{word-break:break-all}

    .links-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(240px,1fr));gap:12px}
    .link-card{display:block;background:#f8fafc;border:1px solid var(--border);border-radius:12px;padding:14px;text-decoration:none;color:var(--text);transition:all .15s}
    .link-card:hover{transform:translateY(-2px);border-color:#cdd6e3;box-shadow:0 6px 16px rgba(37,99,235,.12)}
    .link-head{display:flex;align-items:center;gap:10px;margin-bottom:6px}
    .link-title{font-weight:700;font-size:14px;margin:0}
    .link-desc{font-size:12px;color:var(--muted)}
    .pill{display:inline-block;padding:3px 8px;border-radius:999px;font-size:11px;color:var(--accent);
          background:rgba(37,99,235,.10);border:1px solid rgba(37,99,235,.25)}

    .row{margin-left:0;margin-right:0}
    [class*="col-"]{padding-left:0;padding-right:0;width:100%;float:none}
    a{word-break:break-word}
  </style>
</head>
<body>
<div class="container-narrow">
  <#assign _status = (automation_session.status!''?lower_case)>
  <h2 class="page-title">
    Automation Session
    <span class="chip ${_status}">${_status?has_content?then(_status,'unknown')}</span>
  </h2>

  <div class="stack">
    <!-- Summary -->
    <div class="card">
      <div class="card-header"><span class="pill">Overview</span><h3 class="card-title">Сводка</h3></div>
      <div class="card-body">
        <div class="kv">
          <div class="k">Name</div><div>${automation_session.name!}</div>
          <div class="k">Project</div><div>${automation_session.project_name!}</div>
          <div class="k">Build</div><div>${automation_session.build_name!}</div>
          <div class="k">Reason</div><div>${automation_session.reason!}</div>
          <div class="k">Created At</div><div>${automation_session.created_at!}</div>
          <div class="k">Duration</div><div>${automation_session.duration!} sec</div>
          <div class="k">Session ID</div><div class="mono break">${automation_session.hashed_id!}</div>
          <div class="k">Build ID</div><div class="mono break">${automation_session.build_hashed_id!}</div>
        </div>
      </div>
    </div>

    <!-- Device & OS -->
    <div class="card">
      <div class="card-header"><span class="pill">Environment</span><h3 class="card-title">Device & OS</h3></div>
      <div class="card-body">
        <div class="kv">
          <div class="k">Device</div><div>${automation_session.device!}</div>
          <div class="k">OS</div><div>${automation_session.os!} ${automation_session.os_version!}</div>
          <div class="k">Browser</div><div>${automation_session.browser_version!}</div>
          <div class="k">BrowserStack</div><div>${automation_session.browserstack_status!}</div>
        </div>
      </div>
    </div>

    <!-- App Details -->
    <#if automation_session.app_details??>
    <div class="card">
      <div class="card-header"><span class="pill">Application</span><h3 class="card-title">App Details</h3></div>
      <div class="card-body">
        <div class="kv">
          <div class="k">App Name</div><div>${automation_session.app_details.app_name!}</div>
          <div class="k">Version</div><div>${automation_session.app_details.app_version!}</div>
          <div class="k">File</div><div>${automation_session.app_details.app_filename!}</div>
          <div class="k">Uploaded</div><div>${automation_session.app_details.uploaded_at!}</div>
          <div class="k">App URL</div><div class="mono break">${automation_session.app_details.app_url!}</div>
        </div>
      </div>
    </div>
    </#if>

    <!-- Links & Artifacts -->
    <div class="card">
      <div class="card-header"><span class="pill">Artifacts</span><h3 class="card-title">Logs & Links</h3></div>
      <div class="card-body">
        <div class="links-grid">
          <#if automation_session.browser_url?has_content>
          <a class="link-card" href="${automation_session.browser_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Open</div><div class="link-title">Browser URL</div></div>
            <div class="link-desc">Ссылка на сессию в BrowserStack</div>
          </a>
          </#if>

          <#if automation_session.public_url?has_content>
          <a class="link-card" href="${automation_session.public_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Share</div><div class="link-title">Public URL</div></div>
            <div class="link-desc">Публичная ссылка на сессию</div>
          </a>
          </#if>

          <#if automation_session.logs?has_content>
          <a class="link-card" href="${automation_session.logs}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Logs</div><div class="link-title">Session Logs</div></div>
            <div class="link-desc">Логи сессии</div>
          </a>
          </#if>

          <#if automation_session.video_url?has_content>
          <a class="link-card" href="${automation_session.video_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Video</div><div class="link-title">Recording</div></div>
            <div class="link-desc">Видео прохождения</div>
          </a>
          </#if>

          <#if automation_session.appium_logs_url?has_content>
          <a class="link-card" href="${automation_session.appium_logs_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Appium</div><div class="link-title">Appium Logs</div></div>
            <div class="link-desc">Журнал драйвера</div>
          </a>
          </#if>

          <#if automation_session.device_logs_url?has_content>
          <a class="link-card" href="${automation_session.device_logs_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Device</div><div class="link-title">Device Logs</div></div>
            <div class="link-desc">Системные логи устройства</div>
          </a>
          </#if>

          <#if automation_session.session_terminal_logs_url?has_content>
          <a class="link-card" href="${automation_session.session_terminal_logs_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Terminal</div><div class="link-title">Session Terminal</div></div>
            <div class="link-desc">Терминальные логи сессии</div>
          </a>
          </#if>

          <#if automation_session.build_terminal_logs_url?has_content>
          <a class="link-card" href="${automation_session.build_terminal_logs_url}" target="_blank" rel="noreferrer noopener">
            <div class="link-head"><div class="pill">Terminal</div><div class="link-title">Build Terminal</div></div>
            <div class="link-desc">Терминальные логи билда</div>
          </a>
          </#if>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
