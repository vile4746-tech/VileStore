using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;
using UnityEngine.UIElements;

public class renames : EditorWindow
{
    private string baseName = "";
    private int startindex = 0;

    [MenuItem("新的/批量命名")] 
    public static void ShowWindow()
    {
        renames wnd = GetWindow<renames>();
        wnd.titleContent = new GUIContent("批量命名");
    }

    private void OnGUI()
    {
        GUILayout.Label("批量工具", EditorStyles.boldLabel);
        baseName = EditorGUILayout.TextField("基础名称", baseName);
        startindex = EditorGUILayout.IntField("起始编号", startindex);

        if(GUILayout.Button("重命名它们")){
            GameObject[] ss = Selection.gameObjects;

            for(int i = 0; i < ss.Length; i++)
            {
                ss[i].name = baseName + startindex;
            }


        }

    }

    



}
