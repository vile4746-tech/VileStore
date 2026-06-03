using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using static UnityEngine.UIElements.UxmlAttributeDescription;

public class jietu : EditorWindow
{
    private string folderPath = "";
    private string filename = "";

    [MenuItem("新的/截图工具")]
    public static void put()
    {
        jietu thiswindow = EditorWindow.GetWindow<jietu>();
        thiswindow.titleContent = new GUIContent("截图工具");
    }

    public void OnGUI()
    {
        GUILayout.Label("截图工具", EditorStyles.boldLabel);
        GUILayout.BeginHorizontal();
        folderPath = EditorGUILayout.TextField("路径", folderPath);
        if(GUILayout.Button("浏览", GUILayout.Width(60)))
        {
            folderPath = EditorUtility.OpenFolderPanel("选择你的文件夹", "","");
        }

        GUILayout.EndHorizontal();

        filename = EditorGUILayout.TextField("基础名称", filename);

        if (GUILayout.Button("截图") && folderPath != null && folderPath.Length != 0)
        {
            Debug.Log(folderPath);
            ScreenCapture.CaptureScreenshot(folderPath + "//" + filename + ".png", 1);
        }
    }
}
