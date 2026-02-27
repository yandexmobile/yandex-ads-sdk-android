/*
 * This file is a part of the Yandex Advertising Network
 *
 * Version for Android (C) 2024 YANDEX
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at https://legal.yandex.com/partner_ch/
 */

package com.yandex.ads.sample.tests

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yandex.mobile.ads.common.AdTargeting
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdTargetingProguardTest {

    @Test
    fun testConstructorWithAllParameters() {
        val location = Location("test_provider").apply {
            latitude = 55.7558
            longitude = 37.6173
        }

        val targeting = AdTargeting(
            age = "25",
            gender = "male",
            location = location,
            contextQuery = "test query",
            contextTags = listOf("tag1", "tag2")
        )

        assertNotNull("AdTargeting should be created", targeting)
        assertEquals("Age should be preserved", "25", targeting.age)
        assertEquals("Gender should be preserved", "male", targeting.gender)
        assertNotNull("Location should be preserved", targeting.location)
        assertEquals("ContextQuery should be preserved", "test query", targeting.contextQuery)
        assertEquals(
            "ContextTags should be preserved",
            listOf("tag1", "tag2"),
            targeting.contextTags
        )
    }

    @Test
    fun testConstructorWithPartialParameters() {
        val targeting = AdTargeting(age = "30")

        assertNotNull("AdTargeting should be created with partial params", targeting)
        assertEquals("Age should be preserved", "30", targeting.age)
        assertNull("Gender should be null by default", targeting.gender)
        assertNull("Location should be null by default", targeting.location)
        assertNull("ContextQuery should be null by default", targeting.contextQuery)
        assertNull("ContextTags should be null by default", targeting.contextTags)
    }

    @Test
    fun testConstructorWithNoParameters() {
        val targeting = AdTargeting()

        assertNotNull("AdTargeting should be created with no params", targeting)
        assertNull("Age should be null", targeting.age)
        assertNull("Gender should be null", targeting.gender)
        assertNull("Location should be null", targeting.location)
        assertNull("ContextQuery should be null", targeting.contextQuery)
        assertNull("ContextTags should be null", targeting.contextTags)
    }

    @Test
    fun testBuilderPattern() {
        val targeting = AdTargeting.Builder()
            .setAge("35")
            .setGender("female")
            .setContextQuery("search query")
            .setContextTags(listOf("kotlin", "android"))
            .build()

        assertNotNull("AdTargeting should be created via Builder", targeting)
        assertEquals("Age should be preserved", "35", targeting.age)
        assertEquals("Gender should be preserved", "female", targeting.gender)
        assertNull("Location should be null", targeting.location)
        assertEquals("ContextQuery should be preserved", "search query", targeting.contextQuery)
        assertEquals(
            "ContextTags should be preserved",
            listOf("kotlin", "android"),
            targeting.contextTags
        )
    }

    @Test
    fun testBuilderWithLocation() {
        val location = Location("test_provider").apply {
            latitude = 55.7558
            longitude = 37.6173
        }

        val targeting = AdTargeting.Builder()
            .setLocation(location)
            .build()

        assertNotNull("AdTargeting should be created via Builder", targeting)
        assertNotNull("Location should be preserved", targeting.location)
        assertEquals("Latitude should match", 55.7558, targeting.location!!.latitude, 0.0001)
        assertEquals("Longitude should match", 37.6173, targeting.location!!.longitude, 0.0001)
    }

    @Test
    fun testEmptyBuilder() {
        val targeting = AdTargeting.Builder().build()

        assertNotNull("AdTargeting should be created via empty Builder", targeting)
        assertNull("Age should be null", targeting.age)
        assertNull("Gender should be null", targeting.gender)
        assertNull("Location should be null", targeting.location)
        assertNull("ContextQuery should be null", targeting.contextQuery)
        assertNull("ContextTags should be null", targeting.contextTags)
    }

    @Test
    fun testEquality() {
        val targeting1 = AdTargeting(age = "25", gender = "male")
        val targeting2 = AdTargeting(age = "25", gender = "male")
        val targeting3 = AdTargeting(age = "30", gender = "male")

        assertEquals("Same values should be equal", targeting1, targeting2)
        assertEquals("HashCodes should match", targeting1.hashCode(), targeting2.hashCode())
        assert(targeting1 != targeting3) { "Different values should not be equal" }
    }

    @Test
    fun testBuilderAndConstructorProduceSameResult() {
        val viaConstructor = AdTargeting(
            age = "25",
            gender = "male",
            contextQuery = "query",
            contextTags = listOf("tag")
        )

        val viaBuilder = AdTargeting.Builder()
            .setAge("25")
            .setGender("male")
            .setContextQuery("query")
            .setContextTags(listOf("tag"))
            .build()

        assertEquals(
            "Constructor and Builder should produce equal results",
            viaConstructor,
            viaBuilder
        )
    }
}
