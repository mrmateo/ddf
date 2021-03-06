/*
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 **/
package org.codice.ddf.spatial.kml.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.vividsolutions.jts.geom.GeometryCollection;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Model;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import java.io.InputStream;
import org.junit.Test;

public class TestKmlToJtsGeometryConverter {

  @Test
  public void testConvertNullGeometry() {
    com.vividsolutions.jts.geom.Geometry jtsGeometry = KmlToJtsGeometryConverter.from(null);

    assertThat(jtsGeometry, nullValue());
  }

  @Test
  public void testConvertPointGeometry() {
    InputStream stream = TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlPoint.kml");

    Kml kml = Kml.unmarshal(stream);

    assertThat(kml, notNullValue());

    Point kmlPoint = ((Point) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(kmlPoint, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryPoint =
        KmlToJtsGeometryConverter.from(kmlPoint);
    assertThat(jtsGeometryPoint, instanceOf(com.vividsolutions.jts.geom.Point.class));

    assertSpecificGeometry(kmlPoint, jtsGeometryPoint);
  }

  @Test
  public void testConvertLineStringGeometry() {
    InputStream stream =
        TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlLineString.kml");

    Kml kml = Kml.unmarshal(stream);
    assertThat(kml, notNullValue());

    LineString kmlLineString = ((LineString) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(kmlLineString, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryLineString =
        KmlToJtsGeometryConverter.from(kmlLineString);
    assertThat(jtsGeometryLineString, instanceOf(com.vividsolutions.jts.geom.LineString.class));

    assertSpecificGeometry(kmlLineString, jtsGeometryLineString);
  }

  @Test
  public void testConvertLinearRingGeometry() {
    InputStream stream =
        TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlLinearRing.kml");

    Kml kml = Kml.unmarshal(stream);
    assertThat(kml, notNullValue());

    LinearRing kmlLinearRing = ((LinearRing) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(kmlLinearRing, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryLinearRing =
        KmlToJtsGeometryConverter.from(kmlLinearRing);
    assertThat(jtsGeometryLinearRing, instanceOf(com.vividsolutions.jts.geom.LinearRing.class));

    assertSpecificGeometry(kmlLinearRing, jtsGeometryLinearRing);
  }

  @Test
  public void testConvertPolygonGeometry() {
    InputStream stream = TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlPolygon.kml");

    Kml kml = Kml.unmarshal(stream);
    assertThat(kml, notNullValue());

    Polygon kmlPolygon = ((Polygon) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(kmlPolygon, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryPolygon =
        KmlToJtsGeometryConverter.from(kmlPolygon);
    assertThat(jtsGeometryPolygon, instanceOf(com.vividsolutions.jts.geom.Polygon.class));

    assertSpecificGeometry(kmlPolygon, jtsGeometryPolygon);
  }

  @Test
  public void testConvertMultiGeometry() {
    InputStream stream =
        TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlMultiGeometry.kml");

    Kml kml = Kml.unmarshal(stream);
    assertThat(kml, notNullValue());

    MultiGeometry multiGeometry = ((MultiGeometry) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(multiGeometry, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryCollectionGeometry =
        KmlToJtsGeometryConverter.from(multiGeometry);
    assertThat(jtsGeometryCollectionGeometry, instanceOf(GeometryCollection.class));

    assertSpecificGeometry(multiGeometry, jtsGeometryCollectionGeometry);
  }

  @Test
  public void testConvertModelGeometry() {
    InputStream stream = TestKmlToJtsGeometryConverter.class.getResourceAsStream("/kmlModel.kml");

    Kml kml = Kml.unmarshal(stream);
    assertThat(kml, notNullValue());

    Model model = ((Model) ((Placemark) kml.getFeature()).getGeometry());
    assertThat(model, notNullValue());

    com.vividsolutions.jts.geom.Geometry jtsGeometryPointFromModel =
        KmlToJtsGeometryConverter.from(model);
    assertThat(jtsGeometryPointFromModel, instanceOf(com.vividsolutions.jts.geom.Point.class));

    assertSpecificGeometry(model, jtsGeometryPointFromModel);
  }

  static void assertSpecificGeometry(
      Geometry kmlGeometry, com.vividsolutions.jts.geom.Geometry jtsGeometry) {
    if (kmlGeometry instanceof Point) {
      TestKmlToJtsPointConverter.assertJtsPoint(
          (Point) kmlGeometry, (com.vividsolutions.jts.geom.Point) jtsGeometry);
    }

    if (kmlGeometry instanceof LineString) {
      TestKmlToJtsLineStringConverter.assertTestKmlLineString(
          (LineString) kmlGeometry, (com.vividsolutions.jts.geom.LineString) jtsGeometry);
    }

    if (kmlGeometry instanceof LinearRing) {
      TestKmlToJtsLinearRingConverter.assertJtsLinearRing(
          (LinearRing) kmlGeometry, (com.vividsolutions.jts.geom.LinearRing) jtsGeometry);
    }

    if (kmlGeometry instanceof Polygon) {
      TestKmlToJtsPolygonConverter.assertJtsPolygon(
          (Polygon) kmlGeometry, (com.vividsolutions.jts.geom.Polygon) jtsGeometry);
    }

    if (kmlGeometry instanceof MultiGeometry) {
      TestKmlToJtsMultiGeometryConverter.assertJtsGeometryCollection(
          (MultiGeometry) kmlGeometry, (GeometryCollection) jtsGeometry);
    }

    if (kmlGeometry instanceof Model) {
      TestKmlModelToJtsPointConverter.assertKmlModelToJtsPoint(
          (Model) kmlGeometry, (com.vividsolutions.jts.geom.Point) jtsGeometry);
    }
  }
}
