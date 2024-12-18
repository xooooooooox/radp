package space.x9x.radp.extension.adaptive;

import space.x9x.radp.commons.lang.StringUtils;
import space.x9x.radp.extension.Adaptive;
import space.x9x.radp.extension.ExtensionLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 基于 @Adaptive 的代码生成器
 *
 * @author x9x
 * @since 2024-09-24 14:05
 */
@RequiredArgsConstructor
@Slf4j
public class AdaptiveClassCodeGenerator {

    // 模板字符串，用于生成不同部分的代码
    private static final String FORMAT_CODE_PACKAGE = "package %s;\n";
    private static final String FORMAT_CODE_IMPORT = "import %s;\n;";
    private static final String FORMAT_CODE_CLASS_DECLARATION = "public class %s$Adaptive implements %s {\n";
    private static final String FORMAT_CODE_METHOD_DECLARATION = "public %s %s(%s) throws %s {\n%s\n}\n";
    private static final String FORMAT_CODE_METHOD_ARGUMENT = "%s arg%d";
    private static final String FORMAT_CODE_METHOD_THROWS = "throw %s";
    private static final String FORMAT_CODE_UNSUPPORTED = "throw new UnsupportedOperationException(\"The method %s of interface %s is not adaptive method !\");\n";
    private static final String FORMAT_CODE_URL_NULL_CHECK = "if (arg%d == null) throw new IllegalArgumentException(\"url == null\");\n%s url = arg%d;\n";
    private static final String CLASSNAME_INVOCATION = "org.apache.dubbo.rpc.Invocation"; // TODO 2024/9/24: 为什么是这个 
    private static final String FORMAT_CODE_INVOCATION_ARGUMENT_NULL_CHECK = "if (arg%d == null) throw new IllegalArgumentException(\"invocation == null\"); "
            + "String methodName = arg%d.getMethodName();\n";
    private static final String FORMAT_CODE_EXT_NAME_ASSIGNMENT = "String extName = %s;\n";
    private static final String FORMAT_CODE_EXT_NAME_NULL_CHECK = "if(extName == null) "
            + "throw new IllegalStateException(\"Failed to get extension (%s) name from url (\" + url.toString() + \") use keys(%s)\");\n";
    private static final String FORMAT_CODE_EXTENSION_ASSIGNMENT = "%s extension = (%<s)%s.getExtensionLoader(%s.class).getExtension(extName);\n";
    private static final String FORMAT_CODE_EXTENSION_METHOD_INVOKE_ARGUMENT = "arg%d";


    private final Class<?> type;
    private final String defaultExtName;

    public String generate() {
        if (!hasAdaptiveMethod()) {
            throw new IllegalArgumentException("No adaptive method on extension " + type.getName() + ", refused to create the adaptive class!");
        }
        StringBuilder code = new StringBuilder();
        code.append(generatePackageInfo());
        code.append(generateImports());
        code.append(generateClassDeclaration());

        Method[] methods = type.getMethods();
        for (Method method : methods) {
            code.append(generateMethod(method));
        }
        code.append("}");
        if (log.isDebugEnabled()) {
            log.debug(code.toString());
        }
        return code.toString();
    }

    private String generateMethod(Method method) {
        String methodReturnType = method.getReturnType().getCanonicalName();
        String methodName = method.getName();
        String methodArgs = generateMethodArguments(method);
        String methodThrows = generateMethodThrows(method);
        String methodContent = generateMethodContent(method);
        return String.format(FORMAT_CODE_METHOD_DECLARATION, methodReturnType, methodName, methodArgs, methodThrows, methodContent);
    }

    private String generateMethodContent(Method method) {
        Adaptive adaptiveAnnotation = method.getAnnotation(Adaptive.class);
        StringBuilder code = new StringBuilder(512);
        if (adaptiveAnnotation == null) {
            return generateUnsupported(method);
        }
        int urlTypeIndex = getUrlTypeIndex(method);
        if (urlTypeIndex != -1) {
            code.append(generateUrlNullCheck(urlTypeIndex));
        } else {
            code.append(generateUrlAssignmentIndirectly(method));
        }

        String[] value = getMethodAdaptiveValue(adaptiveAnnotation);
        boolean hasInvocation = hasInvocationArgument(method);
        code.append(generateInvocationArgumentNullCheck(method));
        code.append(generateExtNameAssignment(value, hasInvocation));
        code.append(generateExtNameNullCheck(value));
        code.append(generateExtensionAssignment());
        code.append(generateReturnAndInvocation(method));
        return code.toString();
    }

    private String generateReturnAndInvocation(Method method) {
        String returnStatement = method.getReturnType().equals(void.class) ? "" : "return ";

        String args = IntStream.range(0, method.getParameters().length)
                .mapToObj(i -> String.format(FORMAT_CODE_EXTENSION_METHOD_INVOKE_ARGUMENT, i))
                .collect(Collectors.joining(", "));

        return returnStatement + String.format("extension.%s(%s);%n", method.getName(), args);
    }

    private String generateExtensionAssignment() {
        return String.format(FORMAT_CODE_EXTENSION_ASSIGNMENT, type.getName(), ExtensionLoader.class.getSimpleName(), type.getName());
    }

    private String generateExtNameNullCheck(String[] value) {
        return String.format(FORMAT_CODE_EXT_NAME_NULL_CHECK, type.getName(), Arrays.toString(value));
    }

    private String generateExtNameAssignment(String[] value, boolean hasInvocation) {
        // TODO 2024/9/24: reformat it

        String getNameCode = null;
        for (int i = value.length - 1; i >= 0; --i) {
            if (i == value.length - 1) {
                if (null != defaultExtName) {
                    if (!"protocol".equals(value[i])) {
                        if (hasInvocation) {
                            getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                        } else {
                            getNameCode = String.format("url.getParameter(\"%s\", \"%s\")", value[i], defaultExtName);
                        }
                    } else {
                        getNameCode = String.format("( url.getProtocol() == null ? \"%s\" : url.getProtocol() )", defaultExtName);
                    }
                } else {
                    if (!"protocol".equals(value[i])) {
                        if (hasInvocation) {
                            getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                        } else {
                            getNameCode = String.format("url.getParameter(\"%s\")", value[i]);
                        }
                    } else {
                        getNameCode = "url.getProtocol()";
                    }
                }
            } else {
                if (!"protocol".equals(value[i])) {
                    if (hasInvocation) {
                        getNameCode = String.format("url.getMethodParameter(methodName, \"%s\", \"%s\")", value[i], defaultExtName);
                    } else {
                        getNameCode = String.format("url.getParameter(\"%s\", %s)", value[i], getNameCode);
                    }
                } else {
                    getNameCode = String.format("url.getProtocol() == null ? (%s) : url.getProtocol()", getNameCode);
                }
            }
        }

        return String.format(FORMAT_CODE_EXT_NAME_ASSIGNMENT, getNameCode);
    }

    private String generateInvocationArgumentNullCheck(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return IntStream.range(0, parameterTypes.length)
                .filter(i -> parameterTypes[i].getName().equals(CLASSNAME_INVOCATION))
                .mapToObj(i -> String.format(FORMAT_CODE_INVOCATION_ARGUMENT_NULL_CHECK, i, i))
                .findFirst().orElse("");
    }

    private boolean hasInvocationArgument(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Arrays.stream(parameterTypes).anyMatch(p -> CLASSNAME_INVOCATION.equals(p.getName()));
    }

    private String[] getMethodAdaptiveValue(Adaptive adaptiveAnnotation) {
        String[] value = adaptiveAnnotation.value();
        if (value.length == 0) {
            String splitName = StringUtils.camelToSplitName(type.getSimpleName(), ".");
            value = new String[]{splitName};
        }
        return value;
    }


    private String generateUrlAssignmentIndirectly(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<String, Integer> getterReturnUrl = new HashMap<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            for (Method m : parameterTypes[i].getMethods()) {
                String name = m.getName();
                if ((name.startsWith("get") || name.length() > 3)
                        && Modifier.isPublic(m.getModifiers())
                        && !Modifier.isStatic(m.getModifiers())
                        && m.getParameterTypes().length == 0
                        && m.getReturnType() == URL.class) {
                    getterReturnUrl.put(name, i);
                }
            }
        }
        if (getterReturnUrl.size() <= 0) {
            throw new IllegalArgumentException("Failed to create adaptive class for interface " + type.getName() + ": not found url parameter or url attribute in parameters of method " + method.getName());
        }

        Integer index = getterReturnUrl.get("getUrl");
        if (index != null) {
            return generateGetUrlNullCheck(index, parameterTypes[index], "getUrl");
        } else {
            Map.Entry<String, Integer> entry = getterReturnUrl.entrySet().iterator().next();
            return generateGetUrlNullCheck(entry.getValue(), parameterTypes[entry.getValue()], entry.getKey());
        }
    }

    private String generateGetUrlNullCheck(int index, Class<?> parameterType, String method) {
        return String.format("if (arg%d == null) throw new IllegalArgumentException(\"%s argument == null\");%n",
                index, parameterType.getName()) +
                String.format("if (arg%d.%s() == null) throw new IllegalArgumentException(\"%s argument %s() == null\");%n",
                        index, method, parameterType.getName(), method) +
                String.format("%s url = arg%d.%s();%n",
                        URL.class.getName(), index, method);
    }

    private String generateUrlNullCheck(int index) {
        return String.format(FORMAT_CODE_URL_NULL_CHECK, index, URL.class.getName(), index);
    }

    private int getUrlTypeIndex(Method method) {
        int urlTypeIndex = -1;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].equals(URL.class)) {
                urlTypeIndex = i;
                break;
            }
        }
        return urlTypeIndex;
    }

    private String generateUnsupported(Method method) {
        return String.format(FORMAT_CODE_UNSUPPORTED, method, type.getName());
    }

    private String generateMethodThrows(Method method) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes.length > 0) {
            String list = Arrays.stream(exceptionTypes).map(Class::getCanonicalName).collect(Collectors.joining(", "));
            return String.format(FORMAT_CODE_METHOD_THROWS, list);
        } else {
            return "";
        }
    }

    private String generateMethodArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        return IntStream.range(0, parameterTypes.length)
                .mapToObj(i -> String.format(FORMAT_CODE_METHOD_ARGUMENT, parameterTypes[i].getCanonicalName(), i))
                .collect(Collectors.joining(", "));
    }

    private String generateClassDeclaration() {
        return String.format(FORMAT_CODE_CLASS_DECLARATION, type.getSimpleName(), type.getCanonicalName());
    }

    private String generateImports() {
        return String.format(FORMAT_CODE_IMPORT, ExtensionLoader.class.getName());
    }

    private String generatePackageInfo() {
        return String.format(FORMAT_CODE_PACKAGE, type.getPackage().getName());
    }

    private boolean hasAdaptiveMethod() {
        return Arrays.stream(type.getMethods()).anyMatch(method -> method.isAnnotationPresent(Adaptive.class));
    }
}
