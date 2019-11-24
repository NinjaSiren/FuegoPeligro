package com.mygdx.fuegopeligro.ai.msg;

public enum QAMessageType {

    /**
     * Easy levels QA codes.
     */
    E1(1),
    E2(2),
    E3(3),
    E4(4),
    E5(5),
    E6(6),
    E7(7),
    E8(8),
    E9(9),
    E10(10),
    E11(11),
    E12(12),
    E13(13),
    E14(14),
    E15(15),
    E16(16),
    E17(17),
    E18(18),
    E19(19),
    E20(20),
    E21(21),
    E22(22),
    E23(23),
    E24(24),
    E25(25),
    E26(26),
    E27(27),
    E28(28),
    E29(29),
    E30(30),

    /**
     * Hard levels QA codes.
     */
    H1(31),
    H2(32),
    H3(33),
    H4(34),
    H5(35),
    H6(36),
    H7(37),
    H8(38),
    H9(39),
    H10(40),
    H11(41),
    H12(42),
    H13(43),
    H14(44),
    H15(45),
    H16(46),
    H17(47),
    H18(48),
    H19(49),
    H20(50),
    H21(51),
    H22(52),
    H23(53),
    H24(54),
    H25(55),
    H26(56),
    H27(57),
    H28(58),
    H29(59),
    H30(60),
    H31(61),
    H32(62),
    H33(63),
    H34(64),
    H35(65),
    H36(66),
    H37(67),
    H38(68),
    H39(69),
    H40(70);

    /**
     * The inner code of the message.
     */
    private int code;

    QAMessageType(final int code) {
        this.code = code;
    }

    /**
     * Returns the integer code of the message type.
     *
     * @return The message code.
     */
    public int code() {
        return code;
    }
}
