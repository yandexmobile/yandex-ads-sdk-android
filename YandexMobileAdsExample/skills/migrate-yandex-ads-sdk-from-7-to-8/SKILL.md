# AI Migration Agent Instructions for the Yandex Mobile Ads SDK

## Efficient Migration Strategy

### General StrReplace Rules
To avoid tool errors when modifying files:
1. **Include surrounding context**: Add 1-2 lines before/after for unique matching.
2. **Match exact whitespace**: Preserve all indentation, spaces, newlines exactly.
3. **One logical change per operation**: Don't combine multiple unrelated edits.
4. **Use unique anchors**: Include unique variable/method names or comments.
5. **Verify uniqueness**: Ensure pattern matches only ONE location in file.
6. **Mark manual changes**: Whenever you add new code, layouts, or modify logic during the migration, ALWAYS add a comment `// FIXME_SDK8: Auto-generated during migration, please review.` (or `<!-- FIXME_SDK8: Auto-generated during migration, please review. -->` in XML) to indicate that this code was auto-generated and requires manual review.

### Migration Sequence:

## STEP 0: Update Yandex Ads SDK dependencies

Use latest available versions in repositories :
- Core SDK: [https://repo.maven.apache.org/maven2/com/yandex/android/mobileads/](https://repo.maven.apache.org/maven2/com/yandex/android/mobileads/)
- Mediation bundle: [https://repo.maven.apache.org/maven2/com/yandex/android/mobileads-mediation/](https://repo.maven.apache.org/maven2/com/yandex/android/mobileads-mediation/)
- Mediation adapters: [https://repo.maven.apache.org/maven2/com/yandex/ads/mediation/](https://repo.maven.apache.org/maven2/com/yandex/ads/mediation/)

#### STEP 1: Identify Application Package (MANDATORY - CANNOT SKIP)
Read `app/build.gradle.kts` and find `namespace` value (e.g., `"com.yandex.ads.sample"`).

CRITICAL: Any import starting with this namespace is application code, NOT SDK code.
NEVER replace imports that start with the application namespace.

#### STEP 2: Bulk Import Replacement
Replace SDK imports ONE BY ONE using bulk find-and-replace (e.g., one command per import).
Only process lines that do NOT start with application namespace.

**How to replace imports:**
- Extract the old and new package/class paths from the **Complete Package Structure (SDK 8)** and **Class Mapping** tables below.
- Replace imports ONE AT A TIME with surrounding context.
- Include a blank line or the next import for context to ensure uniqueness.
- Never replace multiple imports in one operation or combine with code changes.

Use IDE's "Replace in Path" feature or command-line tools (sed, PowerShell, etc.)

#### STEP 3: Bulk Simple API & Removed Classes Replacement
Replace simple class/method names ONE BY ONE using bulk find-and-replace (e.g., one command per API):

**How to replace APIs/Classes:**
- Replace ONE method call or class name at a time.
- Include surrounding code (variable declaration, next line, type declaration, assignment) for context.
- Preserve original variable names.

Replace patterns:
- `MobileAds.initialize` → `YandexAds.initialize`
- `AdRequestConfiguration.Builder` → `AdRequest.Builder`
- `ClosableBannerAdEventListener` → `BannerAdEventListener`
- `MobileInstream` → `YandexInstreamAds`
- `BannerAdSize.stickySize` → `BannerAdSize.sticky`
- `BannerAdSize.inlineSize` → `BannerAdSize.inline`

Use IDE's "Replace in Path" feature or command-line tools (sed, PowerShell, etc.)

Optional: If possible, perform STEP 2 and STEP 3 in parallel in one response (both commands are independent). If not, perform sequentially.

Do not start the build.

#### STEP 4: Replace Native Templates

`NativeBannerView` (with `NativeTemplateAppearance`) was a ready-made template removed in SDK 8. The SDK now requires a custom native ad view: `NativeAdView` as the root container + `NativeAdViewBinder` to bind assets to child views.

**Ready-to-use replacement:** The Yandex Mobile Ads public sample (`YandexMobileAdsExample`) provides a complete drop-in implementation. Read the following files from the sample and create them in the partner's project, replacing the sample package `com.yandex.ads.sample` with the app namespace from STEP 1:
- `app/src/main/java/com/yandex/ads/sample/nativeads/NativeAdBannerView.kt` — a custom `FrameLayout` that wires up all native ad asset views via `NativeAdViewBinder`
- `app/src/main/java/com/yandex/ads/sample/common/RatingView.kt` — a `RatingBar` that implements the SDK's `Rating` interface; place it alongside `NativeAdBannerView.kt` and update its package
- `app/src/main/res/layout/native_banner_ad_view.xml` — the full XML layout; update the `RatingView` class reference to match the new package

Add `//FIXME(SDK8): Auto-generated during migration, please review.` to the top of each copied Kotlin file.

Migration Actions:
1. **Extract customizations first**: Search for `NativeTemplateAppearance`, `ButtonAppearance`, `TextAppearance`, `ImageAppearance`, `RatingAppearance`, `SizeConstraint` usages and `app:appearance` in XML. Note the values — you will apply them to the new layout in step 3.
2. **Delete old appearance builder code**: Remove all usages of the builders listed above after extracting the values.
3. **Apply customizations to `native_banner_ad_view.xml`**: For each value extracted in step 1, set the equivalent XML attribute on the corresponding view. If `app:appearance="@style/..."` was used, open that style to extract values first.

| Old customization | View `@id` | XML attribute |
|---|---|---|
| `withTitleAppearance` → `withTextColor` / `withTextSize` | `title` | `android:textColor` / `android:textSize` |
| `withBodyAppearance` → `withTextColor` / `withTextSize` | `body` | `android:textColor` / `android:textSize` |
| `withCallToActionAppearance` → `withTextColor` / `withNormalColor` / `withTextSize` | `call_to_action` | `android:textColor` / `android:backgroundTint` / `android:textSize` |
| `withIconAppearance` → `withWidth` / `withHeight` | `icon` | `android:layout_width` / `android:layout_height` |
| `withRatingAppearance` → `withStarColor` | `rating` | `android:progressTint` |
| `withDomainAppearance` → `withTextColor` / `withTextSize` | `domain` | `android:textColor` / `android:textSize` |
| `withSponsoredAppearance` → `withTextColor` | `sponsored` | `android:textColor` |
| `withWarningAppearance` → `withTextColor` | `warning` | `android:textColor` |
| `SizeConstraint` on root | `native_ad_container` | `android:layout_width` / `android:minHeight` |

4. **Replace `NativeBannerView` in XML** with `NativeAdBannerView` (using the partner's app package). Remove the `app:appearance` attribute. Keep `android:id` and all layout parameters.
5. **Update Kotlin/Java code**:
   - Replace `nativeBannerView.setAd(nativeAd)` → `nativeBannerView.bindNativeAd(nativeAd)`
   - Add `nativeBannerView.release()` before each new load and in `onDestroy()`

Do not start the build.

#### STEP 5: First Build to Identify Complex Cases
Explain to user: build needed to identify complex migration cases.

Only after completing STEP 1-4, start the fast compilation check:
```bash
./gradlew compileDebugSources 2>&1 | tee build.log
```

Important: This is the first build run before iterative fixes.

#### STEP 6: Iterative Fixes & Builder Patterns (apply_diff)
For each compilation error:
1. Read the error from the build log (do not restart the build)
2. Verify that the error is related to Yandex Mobile Ads SDK classes. Do NOT attempt to fix unrelated application errors.
3. Use `apply_diff` to fix it.
4. Fix ALL SDK-related errors from the build log, then rebuild once to verify.

Optional: If fixes are in different files and don't depend on each other, you can perform multiple `apply_diff` operations in parallel in one response.
Important: Do not rebuild after every 5-10 fixes. Fix all SDK-related errors from the current build log, then rebuild once.

Complex cases (require `apply_diff`):
- **Listener refactoring**:
  1. Remove `setAdLoadListener()` call (with context).
  2. Update `loadAd()` to include listener parameter (with context).
  3. Remove interface implementation from class declaration (if needed).
  4. Convert listener to lambda/object (separate operation).
- **AdRequest.Builder with adUnitId parameter**:
  - Old: `AdRequestConfiguration.Builder().setParameters(...).build()`
  - New: `AdRequest.Builder(adUnitId).setParameters(...).build()`
  - Extract `adUnitId` from `setAdUnitId()` call or banner setup. Remove `setAdUnitId()` call after migration.
- **AdTargeting extraction**:
  - Old: `AdRequest.Builder().setAge(25).setGender(Gender.MALE)`
  - New: `AdRequest.Builder(id).setTargeting(AdTargeting.Builder().setAge(25).setGender(Gender.MALE).build())`
  - Move `.setAge()`, `.setGender()`, `.setLocation()` to `AdTargeting.Builder()`.
- **Result.Success/Failure handling**.

---

#### STEP 7 : Verify success build and fix issues

Iterate assemble application until success build, fix found issues between builds.

**(Optional)** Check if the user's project uses this specific mediation adapters (`mobileads-google`, `mobileads-mytarget`, `mobileads-extras`):

- **minSdk 23** — Only required if the user includes specific mediation adapters (e.g. `mobileads-google`, `mobileads-mytarget`, `mobileads-extras`). If they use these adapters and `minSdk` is lower than 23, warn them that a manifest merger failure will occur and suggest raising it to 23. Do not force this if they don't use these adapters.
- **Kotlin 2.1.0+** — Recommended if transitive dependencies ship Kotlin 2.1 metadata (which causes errors with 1.8 compilers). If the user upgrades Kotlin to 2.0+
- **Compose**, if Kotlin 2.1 is updated , ensure they use the `plugin.compose` compiler plugin instead of the old `composeOptions { kotlinCompilerExtensionVersion = ... }`.

Do not automatically edit these Gradle settings unless the user explicitly agrees or encounters a build error related to them.

---

### Tool Usage Rules:

| Task | Tool | Rationale |
|------|------|-----------|
| Import replacement | `execute_command` (sed) | Processes all files in 1 sec |
| Simple API replacement | `execute_command` (sed) | Safe for simple patterns |
| Complex refactoring | `apply_diff` | Requires context and precision |
| Project build | `execute_command` (gradlew compileDebugSources) | Only after bulk replacements |

---

### Recommended Approach (speeds up work):

1. Use `sed` for bulk replacements (imports, simple APIs), but apply them ONE BY ONE (one pattern per command).
2. Group changes: first process imports (one by one), then simple APIs (one by one).
3. Start the compilation check only after bulk replacements
4. Use `apply_diff` only for complex cases
5. Read the build log once, fix several errors, then re-run compilation check

---

## Migrate to Yandex Ads SDK 8

* When I ask to migrate to **Yandex Mobile Ads SDK 8**, start modifying files immediately in the same response without asking for confirmation.
* Assume **Yandex artifact versions** in `build.gradle.kts` are already set by the user. Do not change **Yandex / mediation dependency coordinates** unless the user asks.
* **Exception:** always run **STEP 0** first to check for optional build toolchain updates. Do not force changes unless the user encounters errors.
* Follow the Fast Migration Strategy above to avoid slow iterative approach.

---


### 1. Package Import Changes (Most Important)

Some SDK classes were moved to different packages. Ensure all imports match the **SDK 8** package layout.

Action:
1. Replace old class imports with their new package locations throughout the codebase
2. After replacing imports, remove all unused/old imports to prevent compilation errors
3. Exception: Only user's application package imports remain unchanged (e.g., `com.yourcompany.yourapp.*` - this is application code, not SDK)

### 2. Special Integration Cases

media3.YandexAdsLoader:
- See **Complete Package Structure** → row *Instream Media3 (ExoPlayer)* and **Class Mapping** → *Instream ExoPlayer/Media3 ads insertion* and *Instream Ad Request* (constructor argument type for the loader).

Custom Player Implementations:
- If implementing `InstreamAdPlayer`, add new required methods:
  * `supportedMimeTypes: List<String>`
  * `bindPlayerView(container: FrameLayout)`

---

## Complete Package Structure (SDK 8)

All SDK imports must use `com.yandex.mobile.ads.*` prefix:

| Feature Area | Package | Key Classes |
|--------------|---------|-------------|
| Core/Common | `com.yandex.mobile.ads.common.*` | `YandexAds`, `AdRequest`, `AdRequestError`, `AdError`, `ImpressionData`, `AdBindingResult`, `Result`, `Result.Success`, `Result.Failure` |
| Banner | `com.yandex.mobile.ads.banner.*` | `BannerAdView`, `BannerAdSize`, `BannerAdEventListener` |
| Interstitial | `com.yandex.mobile.ads.interstitial.*` | `InterstitialAd`, `InterstitialAdLoader`, `InterstitialAdLoadListener`, `InterstitialAdEventListener` |
| Rewarded | `com.yandex.mobile.ads.rewarded.*` | `RewardedAd`, `RewardedAdLoader`, `RewardedAdLoadListener`, `RewardedAdEventListener`, `Reward` |
| App Open | `com.yandex.mobile.ads.appopenad.*` | `AppOpenAd`, `AppOpenAdLoader`, `AppOpenAdLoadListener`, `AppOpenAdEventListener` |
| Native Ads | `com.yandex.mobile.ads.nativeads.*` | `NativeAd`, `NativeAdLoader`, `NativeAdLoadListener`, `NativeAdEventListener`, `NativeAdViewBinder`, `NativeAdAssets`, `NativeAdWarning`, `NativeAdOptions`, `SliderAd`, `SliderAdLoader`, `SliderAdLoadListener`, `FeedAd`, `FeedAdAdapter`, `FeedAdAppearance`, `FeedAdLoadListener`, `FeedAdEventListener` |
| Instream | `com.yandex.mobile.ads.instream.*` | `InstreamAd`, `InstreamAdLoader`, `InstreamAdLoadListener`, `InstreamAdListener`, `InstreamAdRequest`, `InstreamAdRequestError`, `YandexInstreamAds`, `InstreamAdBreakType`, `InstreamAdBreakEventListener` |
| Instream Binder | `com.yandex.mobile.ads.instream.binder.*` | `InstreamAdBinder` |
| Instream Ad Break | `com.yandex.mobile.ads.instream.adbreak.*` | `InstreamAdBreak`, `AdBreakData` |
| Instream Player | `com.yandex.mobile.ads.instream.player.*` | `InstreamAdPlayer`, `InstreamAdPlayerListener` |
| Instream Player Content | `com.yandex.mobile.ads.instream.player.content.*` | `VideoPlayer`, `VideoPlayerListener` |
| Instream Player Ad | `com.yandex.mobile.ads.instream.player.ad.*` | `InstreamAdPlayer`, `InstreamAdPlayerListener`, `InstreamAdMimeTypes` |
| Instream Media3 (ExoPlayer) | `com.yandex.mobile.ads.instream.media3.*` | `YandexAdsLoader` — local ad insertion with Media3; import was `instream.exoplayer.YandexAdsLoader` on older 7.x |

---

## Class Mapping

| Feature | Old SDK 7.x | New SDK 8 | Full Package Path |
|---------|-------------|-------------|-------------------|
| Instream SDK Class | `MobileInstream` | `YandexInstreamAds` | `com.yandex.mobile.ads.instream.YandexInstreamAds` |
| Main SDK Class | `MobileAds` | `YandexAds` | `com.yandex.mobile.ads.common.YandexAds` |
| Ad Request | `AdRequestConfiguration` | `AdRequest` | `com.yandex.mobile.ads.common.AdRequest` |
| Ad Request Builder | `AdRequestConfiguration.Builder` | `AdRequest.Builder` | `com.yandex.mobile.ads.common.AdRequest.Builder` |
| Native Ad Request | `NativeAdRequestConfiguration` | `AdRequest` | `com.yandex.mobile.ads.common.AdRequest` |
| Instream Ad Request | `InstreamAdRequestConfiguration` | `InstreamAdRequest` | `com.yandex.mobile.ads.instream.InstreamAdRequest` |
| Instream ExoPlayer/Media3 ads insertion | `YandexAdsLoader` | `YandexAdsLoader` (same class name) | Import/package change: `com.yandex.mobile.ads.instream.exoplayer.YandexAdsLoader` → `com.yandex.mobile.ads.instream.media3.YandexAdsLoader` (use with AndroidX Media3 / `DefaultMediaSourceFactory` local ad insertion; not a replacement for `InstreamAdLoader`) |
| Ad Targeting | N/A (was in AdRequest.Builder) | `AdTargeting` | `com.yandex.mobile.ads.common.AdTargeting` |
| Ad Targeting Builder | N/A | `AdTargeting.Builder` | `com.yandex.mobile.ads.common.AdTargeting.Builder` |
| Native Template View | `NativeBannerView` | `NativeAdView` | `com.yandex.mobile.ads.nativeads.NativeAdView` |
| Native Template Appearance | `NativeTemplateAppearance` | (removed - use custom layouts) | N/A |
| Size Constraint | `SizeConstraint` | (removed) | N/A |
| Banner Event Listener | `ClosableBannerAdEventListener` | `BannerAdEventListener` | `com.yandex.mobile.ads.banner.BannerAdEventListener` |

---

## Method Mapping

| Feature | Old SDK 7.x | New SDK 8 |
|---------|-------------|-------------|
| Initialization | `MobileAds.initialize(...)` | `YandexAds.initialize(...)` |
| Instream Initialization | `MobileInstream.setAdGroupPreloading(...)` | `YandexInstreamAds.setAdGroupPreloading(...)` |
| Banner Setup | `banner.setAdUnitId(id)`<br>`banner.setAdSize(size)`<br>`banner.setBannerAdEventListener(listener)`<br>`banner.loadAd(AdRequest.Builder().build())` | `banner.setAdSize(size)`<br>`banner.setBannerAdEventListener(listener)`<br>`banner.loadAd(AdRequest.Builder(id).build())` |
| Interstitial Load | `loader.setAdLoadListener(listener)`<br>`loader.loadAd(config)` | `loader.loadAd(request, listener)` |
| Native Load | `loader.setNativeAdLoadListener(listener)`<br>`loader.loadAd(config)` | `loader.loadAd(request, listener)` |
| Rewarded Load | `loader.setAdLoadListener(listener)`<br>`loader.loadAd(config)` | `loader.loadAd(request, listener)` |
| App Open Load | `loader.setAdLoadListener(listener)`<br>`loader.loadAd(config)` | `loader.loadAd(request, listener)` |
| Instream Load | `loader.setInstreamAdLoadListener(listener)`<br>`loader.loadInstreamAd(request)` | `loader.loadInstreamAd(request, listener)` |
| Slider Load | `loader.setSliderAdLoadListener(listener)`<br>`loader.loadSlider(config)` | `loader.loadAd(request, listener)` or<br>`loader.loadAd(request, options, listener)` |
| Bulk Native Load | `loader.setNativeBulkAdLoadListener(listener)`<br>`loader.loadAds(config, count)` | `loader.loadAds(request, count, listener)` |
| Banner Size (Sticky) | `BannerAdSize.stickySize(context, width)` | `BannerAdSize.sticky(context, width)` |
| Banner Size (Inline) | `BannerAdSize.inlineSize(context, width, maxHeight)` | `BannerAdSize.inline(context, width, maxHeight)` |
| Ad Show (Interstitial/Rewarded/AppOpen) | `ad.setAdEventListener(listener)`<br>`ad.show(activity)` | `ad.setAdEventListener(listener)`<br>`ad.show(activity)` |
| Native Binding | `try { ad.bindNativeAd(binder) } catch (e: NativeAdException)` | `when (ad.bindNativeAd(binder)) { is AdBindingResult.Failure -> ..., is AdBindingResult.Success -> ... }` |

---

## Property & Type Changes

| Feature | Old SDK 7.x | New SDK 8 | Full Type Path |
|---------|-------------|-------------|----------------|
| Native Warning | `ad.adAssets.warning: String?` | `ad.adAssets.warning: NativeAdWarning?`<br>Use `.value` for text, `.minimumRequiredArea` for size | `com.yandex.mobile.ads.nativeads.NativeAdWarning` |
| AdInfo Data | `ad.info`<br>`ad.adInfo.data` | `ad.adInfo.extraData` | N/A |
| Creative ID | `nativeAd.creativeId` | `ad.adInfo.creatives.first().creativeId` | N/A |
| Campaign ID | `nativeAd.campaignId` | `ad.adInfo.creatives.first().campaignId` | N/A |
| Banner Ad Size | `ad.adInfo.adSize` | `banner.adSize` (only for banner, removed for others) | N/A |

---

## Privacy & Consent Methods

| Feature | Old SDK 7.x | New SDK 8 |
|---------|-------------|-------------|
| Age Restriction | `MobileAds.setAgeRestrictedUser(value)` | `YandexAds.setAgeRestricted(value)` (renamed) |
| Location Consent | `MobileAds.setLocationConsent(value)` | `YandexAds.setLocationTracking(value)` |
| User Consent | `MobileAds.setUserConsent(value)` | `YandexAds.setUserConsent(value)` (unchanged) |

---

## Removed APIs & Alternatives

| Removed API | Alternative | Migration Action |
|-------------|-------------|------------------|
| `NativeBannerView` | `NativeAdBannerView` (from public sample) | Copy `NativeAdBannerView.kt` + `native_banner_ad_view.xml` from the Yandex Mobile Ads public sample. Replace XML tag and update call sites: `setAd()` → `bindNativeAd()`, add `release()`. |
| `NativeTemplateAppearance` | Custom XML layout attributes | Delete all appearance builder code. Visual customization is now done directly via XML attributes on the child views inside `NativeAdView`. |
| `ButtonAppearance` | `Button` XML attributes | Delete builder. Use `android:textColor`, `android:background`, `android:textSize` on your CTA `Button`. |
| `ImageAppearance` | `ImageView` XML attributes | Delete builder. Use `android:scaleType`, `android:layout_width`, `android:layout_height` on your `ImageView`. |
| `LabelAppearance` / `TextAppearance` | `TextView` XML attributes | Delete builder. Use `android:textColor`, `android:textSize`, `android:fontFamily` on your `TextView`. |
| `RatingAppearance` | Custom rating view attributes | Delete builder. Configure your custom rating view directly in XML. |
| `SizeConstraint` | Standard Android layout constraints | Delete builder. Use `android:layout_width`, `android:layout_height`, or `ConstraintLayout` constraints. |
| `ClosableBannerAdEventListener.onAdClosed()` | No direct replacement | Extract business logic to a separate method (e.g., `fun onAdClosed()`). Find where `onAdClosed()` was previously called in the app code and insert the new method call there. Add `// FIXME_SDK8: verify invocation point` comment. |
| `onLeftApplication()` / `onReturnedToApplication()` | Removed from listeners - extract business logic and remove `@Override` / `override` | Delete method overrides completely |
| `AdRequest.Builder().setAge/Gender/Location()` | Move to `AdTargeting.Builder()`, pass via `AdRequest.Builder().setTargeting()` | Extract targeting calls, wrap in `AdTargeting.Builder()`, pass to `setTargeting()` |
| `AdRequest.Builder().setShouldLoadImagesAutomatically()` | Move to `NativeAdOptions` for native ads | Remove from `AdRequest.Builder`, pass to `NativeAdLoader.loadAd(request, options, listener)` where `options = NativeAdOptions.Builder().setShouldLoadImagesAutomatically(value).build()` |
| `BannerAdView.setAdUnitId()` | Pass `adUnitId` to `AdRequest.Builder()` constructor | Remove `setAdUnitId()` call, extract value, pass to `AdRequest.Builder(adUnitId)` |
| `InrollQueueProvider` | Use `instreamAd.instreamAdBreaks.filter { it.adBreakData.type == InstreamAdBreakType.INROLL }` | Remove provider instantiation, replace queue access with filter expression |
| `PauserollQueueProvider` | Use `instreamAd.instreamAdBreaks.filter { it.adBreakData.type == InstreamAdBreakType.PAUSEROLL }` | Remove provider instantiation, replace queue access with filter expression |
| `InstreamAdBreak.invalidate()` | Removed - just remove from memory | Delete method calls completely |

---

## Listener Refactoring Patterns

### Pattern 1: Interstitial/Rewarded/AppOpen Load Listener

Migration Steps:
1. Remove `setAdLoadListener(this)` or `setAdLoadListener(listener)` call
2. Remove interface implementation from class declaration (if class implements listener)
3. Pass listener as second parameter to `loadAd(request, listener)`
4. Convert listener to object expression or lambda if needed

### Pattern 2: Native Ad Load Listener

Migration Steps:
1. Remove `setNativeAdLoadListener(listener)` call
2. Update `loadAd()` to accept two parameters: `loadAd(request, listener)`

### Pattern 3: Instream Ad Load Listener

Migration Steps:
1. Remove `setInstreamAdLoadListener(listener)` call
2. Pass listener directly to `loadInstreamAd(request, listener)`
3. Update error parameter type from `String` to `InstreamAdRequestError`
4. Use `error.description` to get error message

### Pattern 4: Banner Event Listener

Migration Steps:
1. Change listener type from `ClosableBannerAdEventListener` to `BannerAdEventListener`
2. Remove `onAdClosed()` method override completely
3. Extract business logic from `override fun onAdClosed()` to a separate method (e.g., `fun onAdClosed()`)
4. Find the exact place in the application code where `onAdClosed()` was previously being called and insert the call to your new method there.
5. Add a `// FIXME_SDK8: onAdClosed logic migrated from SDK 7.x - verify invocation point` comment at the new call site.

---

# Instream SDK Migration to SDK 8

## Instream Package Changes

| Old SDK 7.x | New SDK 8 | Full Package Path |
|-------------|-------------|-------------------|
| `com.yandex.mobile.ads.instream.InstreamAdBinder` | `com.yandex.mobile.ads.instream.binder.InstreamAdBinder` | `com.yandex.mobile.ads.instream.binder.InstreamAdBinder` |
| `InstreamAdRequestConfiguration` | `InstreamAdRequest` | `com.yandex.mobile.ads.instream.InstreamAdRequest` |
| `InstreamAdBreak` | `adbreak.InstreamAdBreak` | `com.yandex.mobile.ads.instream.adbreak.InstreamAdBreak` |
| `inroll.Inroll` | `adbreak.InstreamAdBreak` | `com.yandex.mobile.ads.instream.adbreak.InstreamAdBreak` |
| `pauseroll.Pauseroll` | `adbreak.InstreamAdBreak` | `com.yandex.mobile.ads.instream.adbreak.InstreamAdBreak` |
| `InrollQueueProvider` | (removed - use `instreamAd.instreamAdBreaks.filter`) | N/A |
| `PauserollQueueProvider` | (removed - use `instreamAd.instreamAdBreaks.filter`) | N/A |

## Instream Method Changes

| Feature | Old SDK 7.x | New SDK 8 |
|---------|-------------|-------------|
| Instream Load | `loader.setInstreamAdLoadListener(listener)`<br>`loader.loadInstreamAd(request)` | `loader.loadInstreamAd(request, listener)` |
| Error Callback | `onInstreamAdFailedToLoad(reason: String)` | `onInstreamAdFailedToLoad(error: InstreamAdRequestError)` - use `error.description` |
| Binder Player | `InstreamAdBinder(context, ad, player, videoPlayer)` | Player is now optional: `InstreamAdBinder(context, ad, null, videoPlayer)` |
| Ad Break Prepare | `adBreak.prepare(player)` | Player is optional: `adBreak.prepare()` or `adBreak.prepare(player)` |
| Queue Access | `InrollQueueProvider(context, ad).queue.poll()` | `instreamAd.instreamAdBreaks.filter { it.adBreakData.type == InstreamAdBreakType.INROLL }.getOrNull(index)` |

## Instream Player Interface Updates

`InstreamAdPlayer` interface has new required methods:

- `override val supportedMimeTypes: List<String>` - return list of supported MIME types
  * Use constants from `InstreamAdMimeTypes`: `MP4`, `WEBM`, `DASH`, `HLS`, etc.
  * If constant not available, fallback to string literal
- `override fun bindPlayerView(container: FrameLayout)` - add your player view to the provided container with MATCH_PARENT layout params

---

## Interface Signature Changes

### Banner Event Listener
- Removed: `onAdClosed()` method

### Interstitial/Rewarded/AppOpen Load Listener
- Changed: Pass listener directly to `loadAd()` instead of `setAdLoadListener()`

### Native Ad Load Listener
- Changed: Pass listener directly to `loadAd()` instead of `setNativeAdLoadListener()`

### Instream Ad Load Listener
- Changed: `onInstreamAdFailedToLoad(reason: String)` → `onInstreamAdFailedToLoad(error: InstreamAdRequestError)`

### Instream Ad Break Event Listener
- Renamed: `InrollEventListener` → `InstreamAdBreakEventListener`
- Renamed methods: `onInrollPrepared()` → `onInstreamAdBreakPrepared()`, etc.
- Changed: `onInrollError(reason: String)` → `onInstreamAdBreakError(reason: String)`

---

## AI Migration Principles

1. Preserve Logic: Make changes only related to Yandex Mobile Ads SDK upgrade to **SDK 8**. Do not change business logic.
2. Preserve Names: Keep variable names, method names (except SDK API)
3. Preserve Order: Keep code order unless API requires change
4. Extract Values: When transforming, extract actual values (like `adUnitId`) and reuse them
5. Combine Calls: When setter + method pattern found, combine into single method call
6. Update Types: When type changes (String → NativeAdWarning), update variable type and access
7. Handle Java: Use `new ClassName() { ... }` for anonymous classes in `.java` files, not Kotlin's `object :`
8. Simplify Queues: Replace Instream queue providers with direct list filtering
9. Optional Players: Consider using `null` for Instream player parameters when SDK default is acceptable
10. Import Cleanup: After migration, remove ALL old imports and add correct `com.yandex.mobile.ads.*` imports

---


## Version Info

- Source: SDK 7.x
- Target: SDK 8
- Updated: March 2026
- Package Structure Change: Added March 2026
- Complete Interface Documentation: Added March 2026
