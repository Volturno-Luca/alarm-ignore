# Salta Sveglie (alarm-ignore)

App + widget per Android che con **un solo tocco** salta tutte le sveglie di oggi,
**senza disattivarle**: suoneranno di nuovo il giorno programmato.

Pensata per chi imposta una "catena" di sveglie al mattino (es. 7:00, 7:05, 7:10…):
appena sveglio, un tocco sul widget e le sveglie successive di oggi non suonano più,
ma restano attive per domani.

## Come funziona

L'app invia al sistema l'intent standard di Android
[`AlarmClock.ACTION_DISMISS_ALARM`](https://developer.android.com/reference/android/provider/AlarmClock#ACTION_DISMISS_ALARM)
con modalità `ALARM_SEARCH_MODE_ALL`. Per una sveglia **ricorrente** questo
"dismiss" salta solo la **prossima** suoneria e lascia la sveglia attiva; per una
sveglia singola la spegne. È lo stesso meccanismo di "Salta la prossima sveglia"
dell'app Orologio di Google.

Nessun permesso speciale, nessun accesso ai dati, nessuna rete: solo il permesso
normale `com.android.alarm.permission.SET_ALARM`. Funziona con l'app **Orologio di
Google** (preinstallata sui Pixel).

## Uso

- **Widget (consigliato):** tieni premuto sulla schermata Home → *Widget* → cerca
  **Salta Sveglie** → trascinalo sulla Home. Da quel momento un tocco = salta tutte
  le sveglie di oggi (nessuna app da aprire, compare solo una conferma).
- **App:** apri *Salta Sveglie* e premi il pulsante grande.

## Come ottenere l'APK **senza Android SDK sul tuo computer** (consigliato)

Il repository include un workflow GitHub Actions (`.github/workflows/build-apk.yml`)
che compila l'APK nel cloud, dove l'Android SDK è già presente. Tu non installi
niente di sviluppo.

1. Su GitHub, apri la scheda **Actions** del repository.
2. Apri il workflow **Build APK** (parte da solo a ogni push; puoi anche avviarlo
   a mano con **Run workflow**).
3. Quando finisce (pochi minuti), scorri in fondo alla pagina della run e scarica
   l'artifact **`SaltaSveglie-debug-apk`** (uno zip con dentro `app-debug.apk`).
4. Passa l'APK sul Pixel 7 (Drive, email, cavo…) e aprilo. Alla prima installazione
   Android chiede di autorizzare l'installazione da "origini sconosciute": conferma.
5. Aggiungi il widget: pressione lunga sulla Home → **Widget** → **Salta Sveglie**.

L'APK di debug è firmato automaticamente (schema v2), quindi è installabile sul
Pixel senza altri passaggi.

## In alternativa: compilazione locale con Android Studio

Se preferisci, apri la cartella in [Android Studio](https://developer.android.com/studio)
(scarica da sé Gradle e l'SDK), collega il Pixel in USB con il **debug USB** attivo
e premi **Run ▶**. Da riga di comando, con l'SDK configurato: `./gradlew installDebug`.

## Alternativa senza nessuna app da compilare

Vuoi il tocco singolo *oggi stesso* senza aspettare la build? Con l'app gratuita
**Automate** (LlamaLab) puoi creare in due blocchi la stessa azione e metterla come
scorciatoia/widget sulla Home:

- Blocco **Start** → blocco **App > Start activity** con:
  - Action: `android.intent.action.DISMISS_ALARM`
  - Extra (String) `android.intent.extra.alarm.SEARCH_MODE` = `android.all`
  - Extra (Boolean) `android.intent.extra.alarm.SKIP_UI` = `true`
- Poi aggiungi il widget/shortcut di Automate che avvia il flow.

A voce funziona sempre: **«Hey Google, salta la prossima sveglia»**. Il vantaggio di
questo progetto è il **tocco singolo** che agisce su **tutte** le sveglie insieme.

## Struttura del progetto

| File | Ruolo |
|------|-------|
| `AlarmSkipper.kt` | Logica: invia l'intent di dismiss a tutte le sveglie |
| `SkipAlarmsWidget.kt` | Widget 1×1 della Home (il tocco singolo) |
| `SkipAlarmsActivity.kt` | Attività invisibile lanciata dal widget, mostra la conferma |
| `MainActivity.kt` | Schermata con pulsante e spiegazione |
