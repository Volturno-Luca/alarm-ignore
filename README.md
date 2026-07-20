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

## Compilazione / installazione

Serve [Android Studio](https://developer.android.com/studio) (che scarica in
automatico Gradle e l'Android SDK).

1. `File > Open` e seleziona questa cartella.
2. Collega il Pixel 7 in USB con il **debug USB** attivo
   (*Impostazioni > Info sul telefono*, tocca 7 volte "Numero build"; poi
   *Opzioni sviluppatore > Debug USB*).
3. Premi **Run ▶**. L'app viene installata sul telefono.

Da riga di comando (con Android SDK configurato):

```bash
./gradlew installDebug
```

L'APK firmato di debug si trova in `app/build/outputs/apk/debug/`.

## Alternativa senza installare nulla

Se non vuoi compilare l'app, puoi dire a voce: **«Hey Google, salta la prossima
sveglia»** / *"dismiss next alarm"*. Il widget di questo progetto rende però
l'operazione un vero **tocco singolo** e agisce su **tutte** le sveglie insieme.

## Struttura del progetto

| File | Ruolo |
|------|-------|
| `AlarmSkipper.kt` | Logica: invia l'intent di dismiss a tutte le sveglie |
| `SkipAlarmsWidget.kt` | Widget 1×1 della Home (il tocco singolo) |
| `SkipAlarmsActivity.kt` | Attività invisibile lanciata dal widget, mostra la conferma |
| `MainActivity.kt` | Schermata con pulsante e spiegazione |
