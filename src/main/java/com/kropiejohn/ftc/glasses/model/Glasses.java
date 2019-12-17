package com.kropiejohn.ftc.glasses.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Glasses model.
 */
public class Glasses {
    /////////////////////
    // Right properties.
    /////////////////////
    private DoubleProperty rightSphere;
    private DoubleProperty rightCylinder;
    private IntegerProperty rightAxis;
    private IntegerProperty rightFocal;

    /////////////////////
    // Left properties.
    /////////////////////
    private DoubleProperty leftSphere;
    private DoubleProperty leftCylinder;
    private IntegerProperty leftAxis;
    private IntegerProperty leftFocal;

    /////////////////////
    // Misc. Properties
    /////////////////////
    private Gender gender;
    private Age age;
    private YesNo bifocals;

    /////////////////////
    // Entry data.
    /////////////////////
    private IntegerProperty number;
    private boolean removed;
    private Date entryDate;

    public Glasses() {
        // Right properties.
        rightSphere = new SimpleDoubleProperty(0);
        rightCylinder = new SimpleDoubleProperty(0);
        rightAxis = new SimpleIntegerProperty(0);
        rightFocal = new SimpleIntegerProperty(0);

        // Left properties.
        leftSphere = new SimpleDoubleProperty(0);
        leftCylinder = new SimpleDoubleProperty(0);
        leftAxis = new SimpleIntegerProperty(0);
        leftFocal = new SimpleIntegerProperty(0);

        // Misc. Properties
        gender = Gender.UNISEX;
        age = Age.ADULT;
        bifocals = YesNo.NO;

        // Entry data.
        number = new SimpleIntegerProperty(0);
    }

    public void reset() {
        // Reset right side.
        rightSphere.set(0);
        rightCylinder.set(0);
        rightAxis.set(0);
        rightFocal.set(0);

        // Reset left side.
        leftSphere.set(0);
        leftCylinder.set(0);
        leftAxis.set(0);
        leftFocal.set(0);

        // Reset other.
        gender = Gender.UNISEX;
        age = Age.ADULT;
        bifocals = YesNo.NO;
    }

    public void copy(Glasses toCopy) {
        // Reset right side.
        rightSphere.set(toCopy.getRightSphere());
        rightCylinder.set(toCopy.getRightCylinder());
        rightAxis.set(toCopy.getRightAxis());
        rightFocal.set(toCopy.getRightFocal());

        // Reset left side.
        leftSphere.set(toCopy.getLeftSphere());
        leftCylinder.set(toCopy.getLeftCylinder());
        leftAxis.set(toCopy.getLeftAxis());
        leftFocal.set(toCopy.getLeftFocal());

        // Reset other.
        gender = toCopy.getGender();
        age = toCopy.getAge();
        bifocals = toCopy.getBifocals();
    }

    public double getRightSphere() {
        return rightSphere.get();
    }

    public DoubleProperty rightSphereProperty() {
        return rightSphere;
    }

    public void setRightSphere(double rightSphere) {
        this.rightSphere.set(rightSphere);
    }

    public double getRightCylinder() {
        return rightCylinder.get();
    }

    public DoubleProperty rightCylinderProperty() {
        return rightCylinder;
    }

    public void setRightCylinder(double rightCylinder) {
        this.rightCylinder.set(rightCylinder);
    }

    public int getRightAxis() {
        return rightAxis.get();
    }

    public IntegerProperty rightAxisProperty() {
        return rightAxis;
    }

    public void setRightAxis(int rightAxis) {
        this.rightAxis.set(rightAxis);
    }

    public int getRightFocal() {
        return rightFocal.get();
    }

    public IntegerProperty rightFocalProperty() {
        return rightFocal;
    }

    public void setRightFocal(int rightFocal) {
        this.rightFocal.set(rightFocal);
    }

    public double getLeftSphere() {
        return leftSphere.get();
    }

    public DoubleProperty leftSphereProperty() {
        return leftSphere;
    }

    public void setLeftSphere(double leftSphere) {
        this.leftSphere.set(leftSphere);
    }

    public double getLeftCylinder() {
        return leftCylinder.get();
    }

    public DoubleProperty leftCylinderProperty() {
        return leftCylinder;
    }

    public void setLeftCylinder(double leftCylinder) {
        this.leftCylinder.set(leftCylinder);
    }

    public int getLeftAxis() {
        return leftAxis.get();
    }

    public IntegerProperty leftAxisProperty() {
        return leftAxis;
    }

    public void setLeftAxis(int leftAxis) {
        this.leftAxis.set(leftAxis);
    }

    public int getLeftFocal() {
        return leftFocal.get();
    }

    public IntegerProperty leftFocalProperty() {
        return leftFocal;
    }

    public void setLeftFocal(int leftFocal) {
        this.leftFocal.set(leftFocal);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public YesNo getBifocals() {
        return bifocals;
    }

    public void setBifocals(YesNo bifocals) {
        this.bifocals = bifocals;
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getPrintText() {
        var sb = new StringBuilder();
        sb.append(System.lineSeparator());
        sb.append(StringUtils.rightPad("", 51, '-')).append(System.lineSeparator());
        var glassesNumberText = "Glasses - " + getNumber();
        sb.append(StringUtils.leftPad(StringUtils.rightPad(glassesNumberText, 25 + glassesNumberText.length() / 2, ' '), 51, ' ')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad("", 51, '-')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad(StringUtils.leftPad("Left", 12, '-'), 25, '-')).append("|");
        sb.append(StringUtils.rightPad(StringUtils.leftPad("Right", 12, '-'), 25, '-')).append(System.lineSeparator());
        // Print out left information
        sb.append(StringUtils.rightPad("Sphere - " + getLeftSphere(), 25, ' ')).append("|");
        sb.append(StringUtils.rightPad("Sphere - " + getRightSphere(), 25, ' ')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad("Cylinder - " + getLeftCylinder(), 25, ' ')).append("|");
        sb.append(StringUtils.rightPad("Cylinder - " + getRightCylinder(), 25, ' ')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad("Axis - " + getLeftAxis(), 25, ' ')).append("|");
        sb.append(StringUtils.rightPad("Axis - " + getRightAxis(), 25, ' ')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad("Focal - " + getLeftFocal(), 25, ' ')).append("|");
        sb.append(StringUtils.rightPad("Focal - " + getRightFocal(), 25, ' ')).append(System.lineSeparator());
        // Print out other data.
        sb.append(StringUtils.rightPad("", 51, '-')).append(System.lineSeparator());
        sb.append(StringUtils.rightPad(StringUtils.leftPad("Gender - " + getGender().getAbbreviation(), 8, ' '), 16, ' '));
        sb.append(StringUtils.rightPad(StringUtils.leftPad("Age - " + getAge().getAbbreviation(), 8, ' '), 16, ' '));
        sb.append(StringUtils.rightPad(StringUtils.leftPad("Bifocals - " + getBifocals().getAbbreviation(), 8, ' '), 16, ' ')).append(System.lineSeparator());

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Glasses{" +
                "rightSphere=" + rightSphere +
                ", rightCylinder=" + rightCylinder +
                ", rightAxis=" + rightAxis +
                ", rightFocal=" + rightFocal +
                ", leftSphere=" + leftSphere +
                ", leftCylinder=" + leftCylinder +
                ", leftAxis=" + leftAxis +
                ", leftFocal=" + leftFocal +
                ", gender=" + gender +
                ", age=" + age +
                ", yesNo=" + bifocals +
                ", removed=" + removed +
                ", entryDate=" + entryDate +
                '}';
    }
}
