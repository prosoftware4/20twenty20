package com.itsronald.twenty2020.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import com.itsronald.twenty2020.BuildConfig.APPLICATION_ID
import com.itsronald.twenty2020.R
import com.itsronald.twenty2020.data.ResourceRepository
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsConfiguration
import com.mikepenz.aboutlibraries.entity.Library
import timber.log.Timber
import javax.inject.Inject

/**
 * Generates an "About App" page and responds to the page's user interactions.
 */
class AboutPresenter
    @Inject constructor(val resources: ResourceRepository)
    : LibsConfiguration.LibsListener, LibsConfiguration.LibsUIListener {

    /**
     * Build an intent for a new About activity.
     *
     * @param context A Context used to generate the new Activity.
     *
     * @return An intent for the About activity that will be created.
     */
    fun buildIntent(context: Context): Intent = buildActivity(context = context).intent(context)


    //region AboutLibraries building

    private fun buildActivity(context: Context): LibsBuilder = LibsBuilder()
            .withAppStyle(context = context)
            .withAboutOptions(context = context)
            .withButtons()
            .withLibs()

    private fun LibsBuilder.withAppStyle(context: Context): LibsBuilder {
        val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightMode == Configuration.UI_MODE_NIGHT_YES

        val activityStyle = if (isNightMode) Libs.ActivityStyle.DARK
                            else Libs.ActivityStyle.LIGHT_DARK_TOOLBAR
        val activityTitle = resources.getString(R.string.about)

        return this.withActivityStyle(activityStyle)
                .withActivityTitle(activityTitle)
                .withUiListener(this@AboutPresenter)
    }

    private fun LibsBuilder.withAboutOptions(context: Context): LibsBuilder {
        val appName = resources.getString(context.applicationInfo.labelRes)
        val appDescription = resources.getString(R.string.about_app_description)
        return this.withAboutAppName(appName)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(appDescription)
                .withLicenseShown(true)
                .withFields(R.string::class.java.fields)
    }

    private fun LibsBuilder.withButtons(): LibsBuilder {
        val sourceCodeButtonTitle = resources.getString(R.string.about_button_source)
        val sourceCodeButtonDescription = resources
                .getString(R.string.about_button_source_description)
        val rateAppButtonTitle = resources.getString(R.string.about_button_rate)
        val rateAppButtonDescription = resources.getString(R.string.about_button_rate_description)

        return this.withAboutSpecial1(sourceCodeButtonTitle)
                .withAboutSpecial1Description(sourceCodeButtonDescription)
                .withAboutSpecial2(rateAppButtonTitle)
                .withAboutSpecial2Description(rateAppButtonDescription)
                .withListener(this@AboutPresenter)
    }

    /**
     * Specifies libraries to include on this screen. While autodetect works in DEBUG mode, the
     * toolchain's minify and Proguard phases strip the build of the info necessary for it to work
     * in the release config.
     */
    private fun LibsBuilder.withLibs(): LibsBuilder {
        val includedLibraries = arrayOf(
                "Dagger2",
                "Gson",
                "LeakCanary",
                "Timber",
                "rxjava",
                "rxandroid",
                "ShowcaseView",
                "appcompat_v7",
                "design",
                "Crashlytics",
                "recyclerview_v7",
                "intellijannotations"
        )
        return this.withAutoDetect(false)
                .withLibraries(*includedLibraries)
    }

    //endregion


    //region LibsConfiguration.LibsUIListener

    override fun preOnCreateView(view: View): View = view

    override fun postOnCreateView(view: View): View {
        val backgroundColor = ContextCompat.getColor(view.context, R.color.windowBackground)
        Timber.v("Setting custom background color on AboutLibraries view: $backgroundColor")
        view.setBackgroundColor(backgroundColor)
        return view
    }

    //endregion


    //region LibsConfiguration.LibsListener

    private val aboutURLString = resources.getString(R.string.url_about)
    private val sourceCodeURLString = resources.getString(R.string.url_github)


    override fun onIconClicked(v: View?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutURLString))
        v?.context?.startActivity(intent)
    }

    override fun onIconLongClicked(v: View?): Boolean = false

    override fun onExtraClicked(v: View?, specialButton: Libs.SpecialButton?): Boolean =
            when (specialButton) {
                Libs.SpecialButton.SPECIAL1 -> onSourceCodeButtonClick(view = v)
                Libs.SpecialButton.SPECIAL2 -> onRateAppButtonClick(view = v)
                Libs.SpecialButton.SPECIAL3 -> false
                null -> throw NullPointerException("Undefined specialButton '$specialButton' clicked!")
            }

    private fun onSourceCodeButtonClick(view: View?): Boolean {
        Timber.i("Opening source code website in browser.")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceCodeURLString))
        view?.context?.startActivity(intent)
        return true
    }

    private fun onRateAppButtonClick(view: View?): Boolean {
        val packageName = APPLICATION_ID
        val storeURI = try {
            Timber.i("Opening app market page with store app.")
            Uri.parse("market://details?id=$packageName")
        } catch (exception: ActivityNotFoundException) {
            Timber.i("Opening app market page in browser.")
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        }

        val intent = Intent(Intent.ACTION_VIEW, storeURI)
        view?.context?.startActivity(intent)
        return true
    }

    override fun onLibraryAuthorClicked(v: View?, library: Library?): Boolean = false

    override fun onLibraryAuthorLongClicked(v: View?, library: Library?): Boolean = false

    override fun onLibraryContentClicked(v: View?, library: Library?): Boolean = false

    override fun onLibraryContentLongClicked(v: View?, library: Library?): Boolean = false

    override fun onLibraryBottomClicked(v: View?, library: Library?): Boolean = false

    override fun onLibraryBottomLongClicked(v: View?, library: Library?): Boolean = false

    //endregion
}